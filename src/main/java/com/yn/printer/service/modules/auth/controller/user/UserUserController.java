package com.yn.printer.service.modules.auth.controller.user;

import com.yn.printer.service.modules.auth.dto.UserAddDTO;
import com.yn.printer.service.modules.auth.dto.UserUpdateDTO;
import com.yn.printer.service.modules.auth.service.UserService;
import com.yn.printer.service.modules.auth.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户控制层
 *
 * @author huabiao
 * @create 2023/12/11  10:25
 **/
@Validated
@Api(value = "UserUserController", tags = "用户端-用户")
@RestController
@RequestMapping("/user/user")
public class UserUserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户-分页")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<UserVO> page(String keyword, Pageable pageable) {
        return userService.page(keyword, pageable);
    }

    @ApiOperation(value = "用户-详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户ID", required = true, dataType = "Long", dataTypeClass = Long.class),
    })
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public UserVO userDetail(@NotNull Long id) {
        return userService.userDetail(id);
    }

    @ApiOperation(value = "用户-新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addUser(@RequestBody @Valid UserAddDTO userAddDTO) {
        userService.addUser(userAddDTO);
    }

    @ApiOperation(value = "用户-修改")

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
    }


    @ApiOperation(value = "用户-删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户ID", required = true, dataType = "Long", dataTypeClass = Long.class),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUser(@NotNull Long id) {
        userService.deleteUser(id);
    }
}