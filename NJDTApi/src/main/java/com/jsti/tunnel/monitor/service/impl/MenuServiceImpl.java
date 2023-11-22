package com.jsti.tunnel.monitor.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsti.tunnel.monitor.bean.Menu;
import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.dao.master.MenuDao;
import com.jsti.tunnel.monitor.dao.master.PermissionDao;
import com.jsti.tunnel.monitor.pojo.RolePermision;
import com.jsti.tunnel.monitor.service.BaseService;
import com.jsti.tunnel.monitor.service.MenuService;

/**
 * 文件名：UserServiceImpl 版本信息：日期：2017/3/30 Copyright  Corporation 2017
 * 版权所有.
 */
@Service 
public class MenuServiceImpl extends BaseService implements MenuService {
  
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private PermissionDao permissionDao;

	@Override
	public List<Menu> getMenus(Menu menu,User user,Integer judge) {
		
		List<Menu> menus = 	menuDao.getMenu(menu);
		if(judge==null || judge.intValue()==0){
			menus=getMenusByPermison(menus,user);
		}
		
		
		List<Menu> mlist = 	new ArrayList<>();
		
		Map<Integer,List<Menu>> map = new HashMap<>();
		
		//找出所有父
		for (Menu m : menus) {
			m.setLabel(m.getMenuname());
			List<Menu> menuList = map.get(m.getParentId());
			if(menuList==null){
				menuList = new ArrayList<>();
			}
			if(m.getParentId()!=null){
				menuList.add(m);
				map.put(m.getParentId(), menuList);
			}else{
				if(!map.containsKey(m.getId()))
					map.put(m.getId(), menuList);
			}
		}
		//找出子
		for (Integer mid : map.keySet()) {
			for (Menu m2 : menus) {
				if(m2.getId().intValue()==mid.intValue()){
					m2.setChildren(map.get(mid));
					if(m2.getParentId()==null){
						mlist.add(m2);
					}
				}
			}
		}
		
		mlist.sort(new Comparator<Menu>() {

			@Override
			public int compare(Menu arg0, Menu arg1) {
				if(arg0.getSortNo() != null && arg1.getSortNo() != null && arg0.getSortNo()>arg1.getSortNo()){
					return 1;
				}
				return -1;
			}
		});
		
		return mlist;
	}

	@Override
	public void insert(Menu menu) {
		menuDao.insert(menu);

    	Permission newPer = new Permission();
    	newPer.setType(1);
    	newPer.setName(menu.getMenuname());
    	newPer.setDescription(menu.getRemark());
    	Integer menuId = commonDao.getLastId().intValue();
    	newPer.setResourceid(menuId);
    	newPer.setStatus(0);

    	if (menu.getParentId() != null) {
			Permission permission = new Permission();
			permission.setType(1);
    		permission.setResourceid(menu.getParentId());
    		permission.setStatus(0);
    		List<Permission> pList = permissionDao.getPermission(permission);
    		if (pList!=null && pList.size()>0) {
    			Permission p = pList.get(0);
    			if (p != null) {
    				newPer.setPid(p.getId());
    			}
    		}
    	} else {
    		newPer.setPid(1);// 默认为根权限
    	}
    	permissionDao.insert(newPer);
	}

	@Override
	public void update(Menu menu) {
		menuDao.update(menu);
		Permission param = new Permission();
		param.setResourceid(menu.getId());
		List<Permission> permissionList = permissionDao.getPermission(param);
		Permission permission = permissionList.size()>0 ? permissionList.get(0): (new Permission());

		permission.setName(menu.getMenuname());
		permission.setDescription(menu.getRemark());
		if (menu.getParentId() != null) {
			Permission param2 = new Permission();
			param2.setType(1);
			param2.setResourceid(menu.getParentId());
			List<Permission> pList = permissionDao.getPermission(param2);
			if (pList!=null && pList.size()>0) {
				Permission p = pList.get(0);
				if (p != null) {
					permission.setPid(p.getId());
				}
			}
		}

		if(permission.getId()==null){
			permission.setResourceid(menu.getId());
			permission.setStatus(0);
			permission.setType(1);
			permissionDao.insert(permission);
		}
		else{
			permissionDao.update(permission);
		}
	}

	@Override
	public void delete(Integer id) {
		Permission param = new Permission();
		param.setResourceid(id);
		List<Permission> permissionList = permissionDao.getPermission(param);
		Permission permission = permissionList.size()>0 ? permissionList.get(0): null;
		if(permission!=null){
			RolePermision rolePermision = new RolePermision();
			rolePermision.setPermission_id(permission.getId());
			permissionDao.deleterolepermision(rolePermision);
			permissionDao.delete(permission.getId());
		}
		menuDao.delete(id);
	}

	@Override
	public List<Menu> getMenuByParent(Menu menu) {
		List<Menu> menus = 	menuDao.getMenuByParent(menu);
		return menus;
	}
	
	
	 private List<Menu> getMenusByPermison(List<Menu> menus,User user){
	    	//判断有权限的资源
	    	//获取权限资源
	    	List<Permission> permissions= permissionDao.findByAdminUserId(user.getId());
	    	Map<Integer,Permission> pmap = new HashMap<>();
	    	for(Permission p : permissions){
	    		pmap.put(p.getResourceid(), p);
	    	}
	    	List<Menu> menulist = new ArrayList<>();
	    	for(Menu m : menus){
	    		//为有权限的资源
	    		if(pmap.get(m.getId())!=null && pmap.get(m.getId()).getPermission_type()==1){
	    			menulist.add(m);
	    		}
	    	}

	    	return menulist;
	    }

	@Override
	public List<Menu> getAllMenu() {
		// TODO Auto-generated method stub
		List<Menu> menus = 	menuDao.getMenu(new Menu());
		return menus;
	}

}
