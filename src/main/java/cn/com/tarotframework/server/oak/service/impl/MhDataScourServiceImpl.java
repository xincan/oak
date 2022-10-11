package cn.com.tarotframework.server.oak.service.impl;

import cn.com.tarotframework.server.oak.mapper.IMhDataScourMapper;
import cn.com.tarotframework.server.oak.mapper.IMhHourDetailMapper;
import cn.com.tarotframework.server.oak.mapper.IMhUserHourMapper;
import cn.com.tarotframework.server.oak.mapper.ISysUserMapper;
import cn.com.tarotframework.server.oak.po.MhHourDetail;
import cn.com.tarotframework.server.oak.po.MhUserHour;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.service.IMhDataScourService;
import cn.com.tarotframework.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class MhDataScourServiceImpl implements IMhDataScourService {

    private IMhDataScourMapper mhDataScourMapper;

    private final ISysUserMapper sysUserMapper;

    private final IMhUserHourMapper mhUserHourMapper;

    private final IMhHourDetailMapper mhHourDetailMapper;


    public MhDataScourServiceImpl(IMhDataScourMapper mhDataScourMapper, ISysUserMapper sysUserMapper, IMhUserHourMapper mhUserHourMapper,
                                  IMhHourDetailMapper mhHourDetailMapper) {
        this.mhDataScourMapper = mhDataScourMapper;
        this.sysUserMapper = sysUserMapper;
        this.mhUserHourMapper = mhUserHourMapper;
        this.mhHourDetailMapper = mhHourDetailMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dataScour(String fileName) {



        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery(SysUser.class)
                .select(SysUser::getUserId, SysUser::getNickName);
        List<SysUser> users = sysUserMapper.selectList(userLambdaQueryWrapper);

        Map<String, List<String>> workdays = DateUtil.getToDayListGroup(fileName.split("-")[0]);
        workdays.entrySet().forEach(System.out::println);

        users.forEach( user -> {
            // 根据用户ID 、填报日期查询
            workdays.forEach((key, value) -> value.forEach( day -> {

                List<MhHourDetail> mhHourDetailList = mhDataScourMapper.getMhHourDetail(user.getUserId(), day);
                if(mhHourDetailList.size() > 0) {
                    double workHour = mhHourDetailList.stream().mapToDouble(hd -> hd.getUseHour().doubleValue()).sum();
                    if(workHour > 8.0) {
                        double ave =  8.0 / mhHourDetailList.size();
                        BigDecimal bd = toBigDecimal(ave);
                        mhHourDetailList.forEach( md -> {
                           LambdaQueryWrapper<MhHourDetail> lambdaQueryWrapper = Wrappers.lambdaQuery();
                            lambdaQueryWrapper.eq(MhHourDetail::getId, md.getId());
                            MhHourDetail mhHourDetail = MhHourDetail.builder().id(md.getId()).useHour(bd).build();
                            mhDataScourMapper.update(mhHourDetail, lambdaQueryWrapper);
                        });
                    }

                    LambdaQueryWrapper<MhUserHour> lambdaQueryWrapper = Wrappers.lambdaQuery();
                    lambdaQueryWrapper.eq(MhUserHour::getUserId, user.getUserId())
                            .eq(MhUserHour::getFillDate, day);
                    MhUserHour mhUserHour = MhUserHour.builder().userId(user.getUserId()).totalHour(new BigDecimal("8")).build();
                    mhUserHourMapper.update(mhUserHour, lambdaQueryWrapper);
                }
            }));

        });

    }

    private BigDecimal toBigDecimal(double num){
        String b = String.format("%.1f", num);
        return new BigDecimal(b);
    }

}
