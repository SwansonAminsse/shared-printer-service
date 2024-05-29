package com.yn.printer.service.common.exception;

/**
 * 异常编码
 * <p>第1位为级别(1为http模块,请求异常等;2为业务模块)</p>
 * <p>2~3位为服务模块</p>
 * <p>4~6位为自增的错误编码</p>
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
public interface YnError {

    /******************************    http    ******************************/
    YnErrorCode YN_000000 = new YnErrorCode("000000", "请刷新重试");

    YnErrorCode YN_100001 = new YnErrorCode("100001", "非法参数");
    YnErrorCode YN_100002 = new YnErrorCode("100002", "暂无数据");
    YnErrorCode YN_100003 = new YnErrorCode("100003", "请勿重复操作");
    YnErrorCode YN_100004 = new YnErrorCode("100004", "未知请求异常");
    YnErrorCode YN_100005 = new YnErrorCode("100005", "接口不可访问");
    YnErrorCode YN_100006 = new YnErrorCode("100006", "服务异常");
    YnErrorCode YN_100007 = new YnErrorCode("100007", "业务创建失败");
    YnErrorCode YN_100008 = new YnErrorCode("100008", "暂无权限处理");

    /******************************    业务    ******************************/

    /**
     * 用户模块
     */
    YnErrorCode YN_200001 = new YnErrorCode("200001", "登录已过期");
    YnErrorCode YN_200002 = new YnErrorCode("200002", "账户不存在");
    YnErrorCode YN_200003 = new YnErrorCode("200003", "密码错误");
    YnErrorCode YN_200004 = new YnErrorCode("200003", "验证码错误");
    YnErrorCode YN_200005 = new YnErrorCode("200005", "手机号不合法");
    YnErrorCode YN_200006 = new YnErrorCode("200006", "身份证号码不合法");
    /**
     * 会员模块
     */
    YnErrorCode YN_300001 = new YnErrorCode("300001", "微信授权失败");
    YnErrorCode YN_300002 = new YnErrorCode("300002", "手机号获取失败");

    /**
     * 设备模块
     */
    YnErrorCode YN_400001 = new YnErrorCode("400001", "当前设备不存在");
    YnErrorCode YN_400002 = new YnErrorCode("400002", "当前设备未设置打印价格");
    YnErrorCode YN_400003 = new YnErrorCode("400003", "暂不支持当前打印方式");
    YnErrorCode YN_400004 = new YnErrorCode("400004", "当前打印方式未设置价格");
    YnErrorCode YN_400005 = new YnErrorCode("400005", "预打印文件不存在");
    YnErrorCode YN_400006 = new YnErrorCode("400006", "预打印文件非PDF格式文件");
    YnErrorCode YN_400007 = new YnErrorCode("400007", "预打印文件无法获取页数");
    YnErrorCode YN_400008 = new YnErrorCode("400008", "当前设备使用中, 请稍后再试");
    YnErrorCode YN_400009 = new YnErrorCode("400009", "当前设备停用中, 请联系客服");
    YnErrorCode YN_400010 = new YnErrorCode("400010", "当前设备不在线, 请联系客服");
    YnErrorCode YN_400011 = new YnErrorCode("400011", "当前设备异常中, 请联系客服");
    YnErrorCode YN_400012 = new YnErrorCode("400012", "当前设备未激活, 请联系客服");
    YnErrorCode YN_400013 = new YnErrorCode("400013", "网络波动，请重试");

    /**
     * 订单模块
     */
    YnErrorCode YN_500001 = new YnErrorCode("500001", "订单不存在");
    YnErrorCode YN_500002 = new YnErrorCode("500002", "打印机剩余纸张数不足");
    YnErrorCode YN_500003 = new YnErrorCode("500003", "订单金额不正确");
    YnErrorCode YN_500004 = new YnErrorCode("500004", "不支持的支付方式");
    YnErrorCode YN_500005 = new YnErrorCode("500005", "余额不足");

    YnErrorCode YN_500006 = new YnErrorCode("500005", "充值失败");

    YnErrorCode YN_500007 = new YnErrorCode("500005", "余额扣减失败");
    YnErrorCode YN_500008 = new YnErrorCode("500008", "异常登录用户无法充值");
    /**
     * 运营模块
     */
    YnErrorCode YN_600001 = new YnErrorCode("600001", "任务不存在");

    /**
     * 文件模块
     */
    YnErrorCode YN_700001 = new YnErrorCode("700001", "文件不存在");
    YnErrorCode YN_700002 = new YnErrorCode("700002", "证件照制作失败");
    YnErrorCode YN_700003 = new YnErrorCode("700003", "证件照排版失败");

}
