package com.admin.web.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.admin.web.config.KISA_SEED_CBC;
import com.admin.web.dto.Menu;
import com.admin.web.dto.User;
import com.admin.web.service.UserService;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Autowired private UserService userService;
	
	@Value("${gs.enc.key}")
	private String encKey;
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		// TODO Auto-generated method stubUserServiceImpl.java
		List<User> mbrList = userService.selectMbrList(loginId);
		boolean notSetMenu = false;
		
		if(mbrList.isEmpty()){
			throw new UsernameNotFoundException("no user");
		}
		
		UserSession userSession = new UserSession(
										mbrList.get(0).getLoginId(),
										mbrList.get(0).getPassword(),
										true,
										true,
										true,
										true,
										getAuthorities(mbrList.get(0).getGrpAlias())
									);
		
		byte[] encData = null;
		try {
			InetAddress local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			
			String data = ip + "," + mbrList.get(0).getLoginId() + "," + mbrList.get(0).getGrpAlias();
			encData = seedEncrypt(encKey, data);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("encData : {}", Base64.encodeBase64String(encData));
		logger.info("decData : {}", seedDecrypt(encKey, encData));
		userSession.setEncData(Base64.encodeBase64String(encData));
		userSession.setLoginId(mbrList.get(0).getLoginId());
		userSession.setGrpAlias(mbrList.get(0).getGrpAlias());

		//설정된 메뉴 값을 확인한다.
		String setMenuNo = mbrList.get(0).getSetMenuNo();
		List<Integer> menuList = new ArrayList<Integer>();
		
		if(setMenuNo.isEmpty()){
			notSetMenu = true;
		}else{
			String[] splitMenu = setMenuNo.split(",");
			
			for(int i=0; i<splitMenu.length; i++){
				menuList.add(Integer.parseInt(splitMenu[i]));
			}
		}
		
		List<Menu> mainMenuList = new ArrayList<Menu>();
		List<Menu> mainMenuGrpList = new ArrayList<Menu>();
		
		if(notSetMenu){
			mainMenuList = null;
			mainMenuGrpList = null;
		}else{
			//설정된 메뉴값을 확인하여 메뉴를 불러온다. 
			mainMenuList = userService.selectMainMenuList(menuList);
			mainMenuGrpList = userService.selectMainMenuGrpList(menuList);
		}
		userSession.setMainMenuList(mainMenuList);
		userSession.setMainMenuGrpList(mainMenuGrpList);
		
		return userSession;
	}

	public Collection<GrantedAuthority> getAuthorities(String grpAlias) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String role = null;
		
		if(grpAlias.toUpperCase().equals("ADMIN")){
			role = "ROLE_ADMIN";
		}else { 
			role = "ROLE_USER";
		}
		
		authorities.add(new SimpleGrantedAuthority(role));
		
		return authorities;
	}
	
	public byte[] seedEncrypt(String encKey, String data){
		byte bszIV[] = {
	        (byte)0x27,  (byte)0x28,  (byte)0x27, (byte)0x6d,  
	        (byte)0x2d,  (byte)0xd5,  (byte)0x4e, (byte)0x29,  
	        (byte)0x2c,  (byte)0x56,  (byte)0xf4, (byte)0x2a,  
	        (byte)0x65,  (byte)0x2a,  (byte)0xae,  (byte)0x08
	    };
		
		byte[] enc = null;
		String charset = "euc-kr";
		
		try{
			enc = KISA_SEED_CBC.SEED_CBC_Encrypt(encKey.getBytes(charset), bszIV, data.getBytes(charset), 0, data.getBytes(charset).length);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return enc;
	}
	
	public String seedDecrypt(String encKey, byte[] enc) {
		byte bszIV[] = {
	        (byte)0x27,  (byte)0x28,  (byte)0x27, (byte)0x6d,  
	        (byte)0x2d,  (byte)0xd5,  (byte)0x4e, (byte)0x29,  
	        (byte)0x2c,  (byte)0x56,  (byte)0xf4, (byte)0x2a,  
	        (byte)0x65,  (byte)0x2a,  (byte)0xae,  (byte)0x08
	    };
	
		byte[] dec = null;
		String charset = "euc-kr";
		String result = null;
		
		try{
			dec = KISA_SEED_CBC.SEED_CBC_Decrypt(encKey.getBytes(charset), bszIV, enc, 0, enc.length);
			result = new String(dec, charset);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return result;
	}
	
//	public static void main(String[] args){
//		 byte pbUserKey[]  = { (byte) 0x1c, (byte) 0x11, (byte) 0x19, (byte) 0x1d, 
//				 (byte) 0x1f, (byte) 0x16, (byte) 0x12, (byte) 0x18,
//				 (byte) 0x11, (byte) 0x19, (byte) 0x1d, (byte) 0x1f, 
//				 (byte) 0x10, (byte) 0x14, (byte) 0x1b, (byte)0x16};
//
//		  byte bszIV[] = {
//	
//		       (byte)0x27,  (byte)0x28,  (byte)0x27, (byte) 0x6d,  (byte)0x2d, (byte) 0xd5,  (byte)0x4e, 
//	
//		       (byte)0x29,  (byte)0x2c,  (byte)0x56, (byte) 0xf4,  (byte)0x2a,  (byte)0x65,  (byte)0x2a,  (byte)0xae,  (byte)0x08
//		       };
//		
//		byte[] enc = null;
//		byte[] dec = null;
//		Base64 encoder = new Base64();
//		String charset = "euc-kr";
//		String data="101643252|A1|테스터|8208026|안녕 12345";	
//		String tet ="abcdefg1234567890ABCDFERE";
//		
//		 try {
//		   /* 암호화 시작 */
//		   //enc = KISA_SEED_CBC.SEED_CBC_Encrypt(pbUserKey, bszIV, data.getBytes(charset), 0, data.getBytes(charset).length);
//			 enc = KISA_SEED_CBC.SEED_CBC_Encrypt(tet.getBytes(charset), bszIV, data.getBytes(charset), 0, data.getBytes(charset).length);
//			 dec = KISA_SEED_CBC.SEED_CBC_Decrypt(tet.getBytes(charset), bszIV, enc, 0, enc.length);
//		  } catch (UnsupportedEncodingException e) {
//
//		   // TODO Auto-generated catch block
//
//		   e.printStackTrace();
//
//		  }
//		
//		 String t = encoder.encodeBase64String(enc);
//		 System.out.println("tttt : " + t);
//		 try {
//			String d = new String(dec, charset);
//			 System.out.println("ddd : " + d);
//
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//	}

}
