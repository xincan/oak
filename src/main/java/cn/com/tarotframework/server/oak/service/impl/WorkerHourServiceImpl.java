package cn.com.tarotframework.server.oak.service.impl;

import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.mapper.*;
import cn.com.tarotframework.server.oak.po.*;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.server.oak.service.IWorkerHourService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class WorkerHourServiceImpl implements IWorkerHourService {

    private final ISysProjectUserMapper sysProjectUserMapper;

    private final IMhProjectHourMapper mhProjectHourMapper;

    private final IMhHourDetailMapper mhHourDetailMapper;

    private final IMhUserHourMapper mhUserHourMapper;

    private final ISysProjectService sysProjectService;

    private final ISysUserService sysUserService;

    private final IMhUserHourService mhUserHourService;


    public WorkerHourServiceImpl(ISysProjectUserMapper sysProjectUserMapper, IMhProjectHourMapper mhProjectHourMapper, IMhHourDetailMapper mhHourDetailMapper, IMhUserHourMapper mhUserHourMapper, ISysProjectService sysProjectService, ISysUserService sysUserService, IMhUserHourService mhUserHourService) {
        this.sysProjectUserMapper = sysProjectUserMapper;
        this.mhProjectHourMapper = mhProjectHourMapper;
        this.mhHourDetailMapper = mhHourDetailMapper;
        this.mhUserHourMapper = mhUserHourMapper;
        this.sysProjectService = sysProjectService;
        this.sysUserService = sysUserService;
        this.mhUserHourService = mhUserHourService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public  void uploadFileData(String filePath) {
        sysProjectService.insert(filePath);
        sysUserService.insert(filePath);
        mhUserHourService.insert(filePath);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFileData(String filePath) {

        //??????excel????????????
        List<ExcelData> excelDataLists = OakDataUtil.getExcelData(filePath);
        // ????????????
        String month = OakDataUtil.getMonth(filePath);

        List<User> users = OakDataUtil.getProjectHours(excelDataLists);

        users.forEach( user -> {

            // ?????????????????? nickname ?????????????????????????????????
            List<SysProjectUser> sysProjectUsers = sysProjectUserMapper.selectProjectUserByProjectIdAndName(user.getUserName());
            if (!CollectionUtils.isEmpty(sysProjectUsers)) {
                sysProjectUsers.forEach( sysProject -> {
                    // ?????? ??????Id?????????Id ?????????????????????????????????
                     List<MhHourDetail> mhHourDetailList = mhHourDetailMapper.getMhHourDetail(sysProject.getProjectId().toString(), sysProject.getUserId().toString(), month);
                     if (!CollectionUtils.isEmpty(mhHourDetailList)) {
                         AtomicReference<Double> userHour = new AtomicReference<>(0.0);
                         mhHourDetailList.forEach( mhHourDetail -> userHour.set(userHour.get() + mhHourDetail.getUseHour().doubleValue()));
                         // ???????????????????????????
                         LambdaQueryWrapper<MhProjectHour> queryWrapper = Wrappers.lambdaQuery();
                         queryWrapper.eq(MhProjectHour::getProjectId, sysProject.getProjectId());
                         MhProjectHour mhProjectHour = mhProjectHourMapper.selectOne(queryWrapper);
                        // ???????????????????????? ?????????????????? - ????????????????????????
                         double manHour = mhProjectHour.getManHour().doubleValue(),
                                 useHour = mhProjectHour.getUseHour().doubleValue();
                         MhProjectHour up = MhProjectHour.builder()
                                 .manHour(BigDecimal.valueOf(manHour <= 0 ? 0.0: mhProjectHour.getManHour().doubleValue() - userHour.get()))
                            .useHour(BigDecimal.valueOf(useHour <= 0 ? 0.0: mhProjectHour.getManHour().doubleValue() - userHour.get())).build();
                         mhProjectHourMapper.update(up, queryWrapper);
                         // ??????????????????
                         mhHourDetailMapper.deleteMhHourDetail(sysProject.getProjectId().toString(), sysProject.getUserId().toString(), month);
                     }
                });
            }
        });
        // ?????? mh_user_hour
        mhUserHourMapper.deleteMhUserHour(month);
        uploadFileData(filePath);

    }

}
