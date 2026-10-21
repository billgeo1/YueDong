<template>
<footer class="site-footer sports-footer">
    <div class="container">
        <div class="row gy-5">
            <div class="col-lg-4 col-md-6 col-12">
                <div class="footer-brand">
                    <span class="brand-mark">
                        <svg viewBox="0 0 48 48" aria-hidden="true">
                            <circle cx="24" cy="24" r="21" fill="#111827"/>
                            <path d="M6.5 24h35M24 3.5v41M12.2 11.8c7.2 6.2 16.4 6.2 23.6 0M12.2 36.2c7.2-6.2 16.4-6.2 23.6 0"
                                  fill="none" stroke="#c9f25d" stroke-width="2.2" stroke-linecap="round"/>
                            <path d="M26.4 10.8 18 25.4h6.1L20.6 38l12-16.4h-7l5.1-10.8Z" fill="#ff6b3d"/>
                        </svg>
                    </span>
                    <div>
                        <strong>跃动体育</strong>
                        <small>COLLEGE SPORTS STORE</small>
                    </div>
                </div>
                <p class="footer-about">面向大学生的校园运动装备集合店。篮球、足球、网球、乒乓球、羽毛球，一站选齐，让每一次上场都更有底气。</p>
            </div>
            <div class="col-lg-2 col-md-6 col-6">
                <div class="footer-heading">Explore</div>
                <ul class="footer-links">
                    <li><a href="/view/index.html">系统首页</a></li>
                    <li><a href="/view/product/productSea.html">装备搜索</a></li>
                    <li><a href="/view/xinwen/xinwenAll.html">运动资讯</a></li>
                    <li><a href="/view/liuyanban/liuyanbanAll.html">运动社区</a></li>
                </ul>
            </div>
            <div class="col-lg-2 col-md-6 col-6">
                <div class="footer-heading">Categories</div>
                <ul class="footer-links">
                    <li><a href="/view/product/productByLeibie.html?leibieId=26">篮球 / 足球</a></li>
                    <li><a href="/view/product/productByLeibie.html?leibieId=27">网球运动</a></li>
                    <li><a href="/view/product/productByLeibie.html?leibieId=28">乒乓球运动</a></li>
                    <li><a href="/view/product/productByLeibie.html?leibieId=29">羽毛球运动</a></li>
                </ul>
            </div>
            <div class="col-lg-4 col-md-6 col-12">
                <div class="footer-heading">Campus Service</div>
                <div class="footer-contact">
                    <strong>动起来，就现在。</strong>
                    校园装备选购 · 正品保障 · 安心售后
                    <br>
                    <i class="bi bi-geo-alt-fill me-1"></i> 大学生体育用品网
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <span>Copyright &copy; {{year}} 大学生体育用品网 · 跃动体育</span>
            <a href="/login.html">系统后台登录 <i class="bi bi-arrow-up-right"></i></a>
        </div>
    </div>
</footer>
</template>

<script>
    module.exports = {
        data: function() {
            return {
                msg: "",
                year: new Date().getFullYear(),
            }
        },
        methods: {
            handeler() {
            }
        }
    }
</script>

<style>
   @import url();
</style>	
