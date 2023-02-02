package com.xinder.user.config;

import com.xinder.api.enums.PermissionsEnums;
import com.xinder.user.config.handler.login.AuthenticationFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

/**
 * Created by sang on 2017/12/17.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "userDetailServiceImpl")
    UserDetailsService userDetailsService;

    //密码处理器
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 认证成功后的处理器
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    // 认证失败后的处理器
    @Autowired
    private AuthenticationFailHandler authenticationFailHandler;

    // 认证异常时的处理器
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // 退出登录处理器
    @Autowired
    private LogoutHandler logoutHandler;

    // 成功退出登录处理器
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;


    /**
     * 配置认证管理器AuthenticationManager。
     * 说白了就是所有 UserDetails 相关的它都管，包含 PasswordEncoder 密码等
     * <p>
     * 在这里关联数据库和security
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("admin/category/all").authenticated()
////                .antMatchers("admin/**", "/reg").hasRole("管理员") ///admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
////                .anyRequest().authenticated()//其他的路径都是登录后即可访问
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                //登录页面
////                .loginPage("https://baidu.com")
//                // 登录请求
//                .loginProcessingUrl("/login")
//                //默认使用的用户名参数
//                .usernameParameter("username")
//                //默认使用的密码参数
//                .passwordParameter("password")
//                .successHandler(authenticationSuccessHandler)  // 认证成功处理器
//                .failureHandler(authenticationFailHandler) // 认证失败处理器
//                .permitAll()
//
//                .and()
//                // 退出
//                .logout()
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler(logoutSuccessHandler)
//                .permitAll()
//                .and()
//                .csrf().disable()
//                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

//        // 解决登录跨域问题
        http.cors(Customizer.withDefaults());


        http.requestMatchers()
                .anyRequest() // 匹配任何请求
//                .and()
//                .formLogin()// 匹配 登录界面
                .and()
                .csrf().disable(); // 跨站攻击防御禁用


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**");
    }


    // 往容器中添加认证管理器(解决无法直接注入的问题，所以需要我们手动注入)
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }





    public static void main(String[] args) {
        System.out.println(PermissionsEnums.ADMINISTRATORS.getValue());
    }
}
