<template>
<div>
<header class="site-header">
    <div class="container">
        <div class="sports-topbar">
            <div class="sports-topbar__message">
                <i class="bi bi-lightning-charge-fill"></i>
                <span>新学期开练 · 为大学生精选每一件运动装备</span>
            </div>
            <div class="sports-account">
                <span v-if="!isLogin">
                    <a href="/view/user/userReg.html"><i class="bi bi-person-plus"></i> 注册</a>
                    <span class="sports-account__divider">/</span>
                    <a href="/view/user/userLogin.html"><i class="bi bi-box-arrow-in-right"></i> 登录</a>
                </span>
                <span v-else>
                    <a href="javaScript:;">Hi，{{user.username}}</a>
                    <span class="sports-account__divider">/</span>
                    <a href="/view/cart/cartMine.html">购物车</a>
                    <span class="sports-account__divider">/</span>
                    <a href="/view/shoucang/shoucangMine.html">收藏</a>
                    <span class="sports-account__divider">/</span>
                    <a href="/auser/index.html">个人中心</a>
                    <span class="sports-account__divider">/</span>
                    <a href="javaScript:;" @click="userLogout()">退出</a>
                </span>
            </div>
        </div>
    </div>
</header>

<!-- 导航 -->
<nav class="navbar navbar-expand-lg sports-navbar">
    <div class="container">
        <a class="navbar-brand" href="/view/index.html" aria-label="跃动体育首页">
            <span class="brand-mark">
                <svg viewBox="0 0 48 48" aria-hidden="true">
                    <circle cx="24" cy="24" r="21" fill="#111827"/>
                    <path d="M6.5 24h35M24 3.5v41M12.2 11.8c7.2 6.2 16.4 6.2 23.6 0M12.2 36.2c7.2-6.2 16.4-6.2 23.6 0"
                          fill="none" stroke="#c9f25d" stroke-width="2.2" stroke-linecap="round"/>
                    <path d="M26.4 10.8 18 25.4h6.1L20.6 38l12-16.4h-7l5.1-10.8Z" fill="#ff6b3d"/>
                </svg>
            </span>
            <span class="brand-copy">
                <strong>跃动体育</strong>
                <small>大学生体育用品网</small>
            </span>
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#sportsNavbar"
                aria-controls="sportsNavbar" aria-expanded="false" aria-label="展开导航">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="sportsNavbar">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/view/index.html" :class="{active: isCurrent('/view/index.html')}">系统首页</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/view/gonggao/gonggaoAll.html" :class="{active: isCurrent('/view/gonggao/')}">系统公告</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/view/xinwen/xinwenAll.html" :class="{active: isCurrent('/view/xinwen/')}">运动资讯</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="sportsCategoryMenu" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">商品分类</a>
                    <ul class="dropdown-menu dropdown-menu-light" aria-labelledby="sportsCategoryMenu">
                        <li v-for="(leibie, index) in leibieList" :key="index">
                            <a :href="'/view/product/productByLeibie.html?leibieId='+leibie.id" class="dropdown-item">
                                <i class="bi bi-arrow-up-right me-1"></i>{{leibie.mingcheng}}
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/view/liuyanban/liuyanbanAll.html" :class="{active: isCurrent('/view/liuyanban/')}">运动社区</a>
                </li>
            </ul>
            <a class="nav-search" href="/view/product/productSea.html">
                <i class="bi bi-search"></i> 搜索装备
            </a>
        </div>
    </div>
</nav>
<!-- 导航 -->
</div>
</template>



<script>

    module.exports = {
        data: function() {
            return {
                isLogin: false,
                leibieList:[],
                user: {},
                path: "",
            }
        },
        methods: {
            isCurrent(prefix) {
                if (!this.path) {
                    return false;
                }
                if (prefix === "/view/index.html") {
                    return this.path === "/" || this.path === "/index.html" || this.path === "/view/index.html";
                }
                return this.path.indexOf(prefix) === 0;
            },

            handeler() {
                //this.count = this.count + 1;
            },
            
            leibieAll()
			{
	           axios({
						method: 'POST',
						url: '/leibieAll',
						/* data: this.laoshi, */
						/* params:{
							username:this.laoshi.userName,
							password:this.laoshi.userPw,
						} */
						}).then(data => {
							if (data.data.state == 66)
							{
								this.leibieList=data.data.data;
								//alert(this.leibieList.length);
							} 
						}).catch(error => {
							alert(error);
				})
			},

            userLogout(){
               if(confirm('确实要退出吗?')){
                  sessionStorage.removeItem("user");
                  window.location.href="/index.html";
               }
               
               
            },
        },
        
        
        

        created()
        {
            this.path = window.location.pathname;
            this.leibieAll();
        
            var userJsonString = sessionStorage.getItem("user");
            if(userJsonString !=null)//判断是否登录
            {
                var user=JSON.parse(userJsonString);
                this.user=user;
                this.isLogin=true;
            }
            else
            {
                 this.isLogin=false;
            }

            
        },
        
        mounted() 
        {
            /* let script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = 'js/comm.js';
            document.body.appendChild(script); */
        },

    }


</script>


<style>
   @import url();
</style>

