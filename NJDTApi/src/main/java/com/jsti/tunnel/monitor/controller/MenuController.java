package com.jsti.tunnel.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsti.system.err.BusinessException;
import com.jsti.tunnel.monitor.bean.Menu;
import com.jsti.tunnel.monitor.bean.Permission;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.MenuService;
import com.jsti.tunnel.monitor.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 文件名：TestController
 * .
 */
/**
 * @author wenhongquan
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 添加菜单
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/addMenu",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String addMenu(@RequestBody Menu menu) throws Exception{
    	menu.setStatus(0);
    	menuService.insert(menu);
    	
    	return returnResult(0, "添加成功", null);
    }
    
    /**
     * 修改菜单
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/updateMenu/{menuid}",produces="application/json;charset=UTF-8")
    public String updateMenu(@RequestBody Menu menu,@PathVariable int menuid){
        try{
        	menu.setId(menuid);
        	menuService.update(menu);
        	return super.returnResult(0, "修改成功", null);
        }catch(Exception e){
        	throw new BusinessException(20003);
        }
    }
    
    /**
     * 删除菜单（软删除）
     * @return
     * String
     * @exception
     * @since  1.0.0
     */
    @RequestMapping(value = "/deleteMenu/{menuid}",produces="application/json;charset=UTF-8")
    public String deleteMenu(@PathVariable Integer menuid){
        try{
        	menuService.delete((menuid));
        	return returnResult(0, "删除成功", null);
        }catch(Exception e){
        	throw new BusinessException(20002);
        }
    }
    
    /**
     * 获取所有菜单
     * @param page
     * @param size
     * @return
     */

    @RequestMapping(value = "/get/all",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getAllMenu(HttpServletRequest request,@RequestParam(required=false) Integer judge){
        try{
        	User u = getCurrentUser(request);
        	List<Menu> menus= menuService.getMenus(new Menu(),u,judge);
        	return returnResult(0, "获取成功", menus);
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
  
    @RequestMapping(value = "/getMenuByParent/{pid}/{page}/{size}",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String getMenuByParent(@PathVariable Integer pid,@PathVariable Integer page,@PathVariable Integer size, @RequestBody Menu menu){
        try{
        	PageHelper.startPage(page, size); 
//        	Menu menu =new Menu();
        	menu.setParentId(pid);
        	List<Menu> menus= menuService.getMenuByParent(menu);
        	
        	return returnResult(0, "获取成功", new PageInfo(menus));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getMenuById/{menuid}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getMenuById(@PathVariable Integer menuid,HttpServletRequest request){
        try{
        	User u = getCurrentUser(request);
        	Menu menu = new Menu();
        	menu.setId(menuid);
        	
        	List<Menu> menus = menuService.getMenus(menu,u,0);
        	if(menus==null||menus.size()<1 ) throw  new BusinessException(20001);
        	return returnResult(0, "获取成功", menus.get(0));
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
    @RequestMapping(value = "/getMenuByUser",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String getMenuByUser(HttpServletRequest request){
        try{
        	User u = super.getCurrentUser(request);
        	List<Permission> permissions= permissionService.getPermissions(u.getId());
        	List<Menu> menus = menuService.getAllMenu();
        	Set<Integer> menuIdSet = new HashSet<Integer>();
        	for (Permission p : permissions) {
        		if (p.getType() == 1) {
        			menuIdSet.add(p.getResourceid());
        		}
        	}
        	List<Menu> newMenuList = new ArrayList<>();
        	for (Menu m: menus) {
        		if (menuIdSet.contains(m.getId())) {
        			newMenuList.add(m);
        		}
        	}

            List<Menu> mlist = 	new ArrayList<>();

            Map<Integer,List<Menu>> map = new HashMap<>();

            //找出所有父
            for (Menu m : newMenuList) {
                m.setLabel(m.getMenuname());
                List<Menu> menuList = map.get(m.getParentId());
                if(menuList==null){
                    menuList = new ArrayList<>();
                }
                if(m.getParentId()!=null ){
                    menuList.add(m);
                    map.put(m.getParentId(), menuList);//将当前节点放入map，key为父节点id
                }else{
                    if(!map.containsKey(m.getId()))
                        map.put(m.getId(), menuList);
                }
            }

            //找出子
            for (Integer mid : map.keySet()) {
                for (Menu m2 : newMenuList) {
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


        	return returnResult(0, "获取成功", mlist.get(0).getChildren());
        }catch(Exception e){
        	throw new BusinessException(20001);
        }
    }
    
}
