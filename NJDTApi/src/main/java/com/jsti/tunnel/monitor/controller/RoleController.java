package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.jsti.system.err.BusinessException;
import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.pojo.RoleData;
import com.jsti.tunnel.monitor.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：TestController
 * .
 */
/**
 * @author wenhongquan
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	

	/**
	 * 添加角色
	 * 
	 * @return String
	 * @exception @since
	 *                1.0.0
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String addRole(@RequestBody Role role) throws Exception {
		roleService.insert(role);
		return returnResult(0, "添加成功", null);
	}

	/**
	 * 修改角色
	 * 
	 * @return String
	 * @exception @since
	 *                1.0.0
	 */
	@RequestMapping(value = "/updateRole/{roleid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String updateRole(@RequestBody Role role, @PathVariable int roleid) {
		try {
			role.setId(roleid);
			roleService.update(role);
			return super.returnResult(0, "修改成功", null);
		} catch (Exception e) {
			throw new BusinessException(20003);
		}
	}

	/**
	 * 删除角色（软删除）
	 * 
	 * @return String
	 * @exception @since
	 *                1.0.0
	 */
	@RequestMapping(value = "/deleteRole/{roleid}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String deleteRole(@PathVariable Integer roleid) {
		try {
			roleService.delete((roleid));
			return returnResult(0, "删除成功", null);
		} catch (Exception e) {
			throw new BusinessException(20002);
		}
	}

	/**
	 * 获取所有角色
	 * @return
	 */
	@RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getAllRole(HttpServletRequest request) {
		try {
			User u = getCurrentUser(request);
			List<Role> roles = roleService.getRoles(new Role(),u);

			return returnResult(0, "获取成功", roles);
		} catch (Exception e) {
			throw new BusinessException(20001);
		}
	}

	
	/**
	 * 获取所有角色
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/get/alldata/{page}/{size}", produces = "application/json;charset=UTF-8")
	public String getAllRoleData(@PathVariable Integer page, @PathVariable Integer size, @RequestBody Role role) {
		try {
			PageHelper.startPage(page, size);
			List<RoleData> roles = roleService.getRoleDatas(role);
			//return returnResult(0, "获取成功", new PageInfo(roles));
			return  returnSuccessPageResult(roles);
		} catch (Exception e) {
			throw new BusinessException(20001);
		}
	}

	@RequestMapping(value = "/roleBindPermission/{roleId}/{permissions}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String roleBindPermission(@PathVariable Integer roleId, @PathVariable String permissions) {
		String[] permissionids = permissions.split(",");
		List<Integer> pids = new ArrayList<>();
		for (String id : permissionids) {
			if (!id.isEmpty()) {
				try {
					Integer idt = Integer.parseInt(id);
					pids.add(idt);
				} catch (Exception e) {
					throw new BusinessException(20001);
				}
			}
		}
		// 先删除旧的绑定权限
		roleService.roleUnBindPermission(roleId);
		for (Integer id : pids) {
			roleService.roleBindPermission(roleId, id);
		}

		return returnResult(0, "授权成功", null);
	}

	@RequestMapping(value = "/roleBindUser/{roleId}/{users}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public String roleBindUser(@PathVariable Integer roleId, @PathVariable String users) {
		String[] userids = users.split(",");
		List<Integer> uids = new ArrayList<>();
		for (String id : userids) {
			if (!id.isEmpty()) {
				try {
					Integer idt = Integer.parseInt(id);
					uids.add(idt);
				} catch (Exception e) {
					throw new BusinessException(20001);
				}
			}
		}
		for (Integer id : uids) {
			roleService.roleBindUser(roleId, id);
		}
		return returnResult(0, "绑定成功", null);
	}

	@RequestMapping(value = "/getRoleByDept/{deptId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getRoleByDept(@PathVariable Integer deptId) {

		List<Role> roles = roleService.getRoleByDept(deptId);

		return returnResult(0, "获取成功", roles);
	}

	@RequestMapping(value = "/getRoleByUser/{userId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getRoleByUser(@PathVariable Integer userId) {

		List<Role> roles = roleService.getRoleByUser(userId);

		return returnResult(0, "获取成功", roles);
	}

	@RequestMapping(value = "/getRoleByPosition/{positionId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getRoleByPosition(@PathVariable Integer positionId) {
		List<Role> roles = roleService.getRoleByPosition(positionId);

		return returnResult(0, "获取成功", roles);
	}
	
	@RequestMapping(value = "/getRoleByPermision/{permisionid}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String getRoleByPermisionidn(@PathVariable Integer permisionid) {
		List<Role> roles = roleService.getRoleByPermision(permisionid);

		return returnResult(0, "获取成功", roles);
	}


}
