<template>
<section class="yd-agent" :class="{ 'yd-agent--open': open }">
    <button class="yd-agent__launcher" type="button" :aria-expanded="String(open)"
            aria-controls="ydAgentPanel" aria-label="打开跃动运动顾问" @click="togglePanel">
        <span class="yd-agent__launcher-ball" aria-hidden="true">
            <i></i><i></i><i></i>
        </span>
        <span v-if="!open" class="yd-agent__launcher-copy">
            <strong>问跃动</strong>
            <small>AI SPORTS ADVISOR</small>
        </span>
        <span v-else class="yd-agent__launcher-close" aria-hidden="true">×</span>
    </button>

    <aside v-show="open" id="ydAgentPanel" class="yd-agent__panel" role="dialog"
           aria-modal="false" aria-label="跃动运动顾问">
        <header class="yd-agent__header">
            <div class="yd-agent__identity">
                <span class="yd-agent__avatar" aria-hidden="true">
                    <span></span>
                </span>
                <span class="yd-agent__identity-copy">
                    <small>YUE DONG · AGENT 01</small>
                    <strong>跃动运动顾问</strong>
                    <em><i></i> 实时商品与订单工具已连接</em>
                </span>
            </div>
            <button class="yd-agent__header-action" type="button" title="清空当前对话"
                    aria-label="清空当前对话" @click="resetChat">↺</button>
        </header>

        <div ref="messages" class="yd-agent__messages" aria-live="polite">
            <div v-if="messages.length === 0" class="yd-agent__welcome">
                <div class="yd-agent__court" aria-hidden="true">
                    <span class="yd-agent__court-ball"></span>
                    <span class="yd-agent__court-line"></span>
                    <span class="yd-agent__court-label">PLAY SMART</span>
                </div>
                <p class="yd-agent__eyebrow">你的校园运动装备搭子</p>
                <h2>先定场景，<br>再帮你挑对装备。</h2>
                <p class="yd-agent__intro">我能读取商城的真实商品、库存与你的订单，也能先写一份加购草案。涉及购物车等写操作时，会停下来等你确认。</p>
                <div class="yd-agent__features">
                    <span><i>01</i>实时检索</span>
                    <span><i>02</i>订单查询</span>
                    <span><i>03</i>确认执行</span>
                </div>
            </div>

            <article v-for="(message, index) in messages" :key="message.id"
                     class="yd-agent__message" :class="'yd-agent__message--' + message.role">
                <div v-if="message.role === 'user'" class="yd-agent__user-bubble">
                    {{ message.text }}
                </div>

                <template v-else>
                    <div class="yd-agent__agent-row">
                        <span class="yd-agent__mini-avatar" aria-hidden="true">跃</span>
                        <div class="yd-agent__answer">
                            <p>{{ message.text }}</p>

                            <div v-if="message.response && message.response.steps"
                                 class="yd-agent__steps" aria-label="执行过程">
                                <span v-for="(step, stepIndex) in message.response.steps"
                                      :key="stepIndex" :class="'is-' + step.status">
                                    <i></i>{{ step.label }}
                                </span>
                            </div>

                            <div v-if="needsLogin(message)" class="yd-agent__login">
                                <p>登录后即可查询订单或安全加购。</p>
                                <a href="/view/user/userLogin.html">去登录 <span>→</span></a>
                            </div>

                            <div v-if="message.response && message.response.products"
                                 class="yd-agent__products">
                                <a v-for="product in message.response.products" :key="product.id"
                                   class="yd-agent__product" :href="product.detailUrl || ('/view/product/productDetailQian.html?id=' + product.id)">
                                    <span class="yd-agent__product-image">
                                        <img :src="product.image" :alt="product.title">
                                        <i v-if="product.stock > 0">有货</i>
                                        <i v-else class="is-empty">缺货</i>
                                    </span>
                                    <span class="yd-agent__product-copy">
                                        <small>{{ product.categoryName || '校园运动装备' }}</small>
                                        <strong>{{ product.title }}</strong>
                                        <span>
                                            <b>¥{{ product.price }}</b>
                                            <em>库存 {{ product.stock }}</em>
                                        </span>
                                    </span>
                                    <i class="yd-agent__product-arrow">↗</i>
                                </a>
                            </div>

                            <div v-if="message.response && message.response.orders"
                                 class="yd-agent__records">
                                <div v-for="order in message.response.orders" :key="order.id"
                                     class="yd-agent__record">
                                    <span>
                                        <small>订单 #{{ order.id }}</small>
                                        <strong>{{ order.bianhao }}</strong>
                                    </span>
                                    <span class="yd-agent__record-meta">
                                        <b>¥{{ order.zongjine }}</b>
                                        <em>{{ order.zt }}</em>
                                    </span>
                                </div>
                            </div>

                            <div v-if="message.response && message.response.data"
                                 class="yd-agent__metrics">
                                <template v-for="(value, key) in message.response.data">
                                    <div v-if="isScalar(value)" :key="key" class="yd-agent__metric">
                                        <small>{{ metricLabel(key) }}</small>
                                        <strong>{{ metricValue(key, value) }}</strong>
                                    </div>
                                </template>
                            </div>

                            <div v-if="message.response && message.response.items"
                                 class="yd-agent__records">
                                <div v-for="(item, itemIndex) in message.response.items"
                                     :key="itemIndex" class="yd-agent__record">
                                    <span>
                                        <small>{{ item.categoryName || '体育用品' }}</small>
                                        <strong>{{ item.title }}</strong>
                                    </span>
                                    <span class="yd-agent__record-meta">
                                        <b v-if="item.price !== undefined">¥{{ item.price }}</b>
                                        <em v-if="item.stock !== undefined">库存 {{ item.stock }}</em>
                                        <em v-if="item.sales !== undefined">销量 {{ item.sales }}</em>
                                    </span>
                                </div>
                            </div>

                            <div v-if="message.response && message.response.confirmation"
                                 class="yd-agent__confirm"
                                 :class="{ 'is-done': message.confirmationStatus === 'done' }">
                                <div class="yd-agent__confirm-head">
                                    <span>需要你的确认</span>
                                    <i>{{ riskText(message.response.confirmation.riskLevel) }}</i>
                                </div>
                                <strong>{{ message.response.confirmation.title }}</strong>
                                <p>{{ message.response.confirmation.impact }}</p>
                                <div class="yd-agent__confirm-meta">
                                    <span>10 分钟内有效</span>
                                    <span>令牌已绑定当前账号</span>
                                </div>
                                <button v-if="message.confirmationStatus !== 'done'"
                                        type="button"
                                        :disabled="message.confirming"
                                        @click="confirmAction(message)">
                                    {{ message.confirming ? '正在执行…' : '确认并执行' }}
                                    <span>→</span>
                                </button>
                                <div v-else class="yd-agent__confirm-success">
                                    <i>✓</i> 已确认执行，商城数据已同步
                                </div>
                            </div>

                            <div v-if="message.response && message.response.traceId"
                                 class="yd-agent__trace">
                                TRACE {{ shortTrace(message.response.traceId) }}
                            </div>
                        </div>
                    </div>
                </template>
            </article>

            <article v-if="loading" class="yd-agent__message yd-agent__message--assistant">
                <div class="yd-agent__agent-row">
                    <span class="yd-agent__mini-avatar" aria-hidden="true">跃</span>
                    <div class="yd-agent__thinking">
                        <span></span><span></span><span></span>
                        <em>大模型正在理解需求并读取实时数据</em>
                    </div>
                </div>
            </article>
        </div>

        <div v-if="suggestions.length" class="yd-agent__suggestions" aria-label="推荐问题">
            <button v-for="suggestion in suggestions" :key="suggestion" type="button"
                    :disabled="loading" @click="askSuggestion(suggestion)">
                {{ suggestion }}
            </button>
        </div>

        <form class="yd-agent__composer" @submit.prevent="sendMessage">
            <label for="ydAgentInput">想问点什么？</label>
            <div class="yd-agent__composer-row">
                <textarea id="ydAgentInput" ref="input" v-model="draft" rows="1"
                          maxlength="300" placeholder="例如：200元以内篮球装备"
                          :disabled="loading" @keydown="handleKeydown"></textarea>
                <button type="submit" :disabled="loading || !draft.trim()" aria-label="发送问题">
                    <span>发送</span><i>↑</i>
                </button>
            </div>
            <p>回答基于商城实时数据 · 写操作必须人工确认</p>
        </form>
    </aside>
</section>
</template>

<script>
module.exports = {
    name: 'agent-console',
    data: function () {
        return {
            open: false,
            draft: '',
            loading: false,
            messages: [],
            suggestions: ['200元以内篮球装备', '查一下我的订单', '羽毛球拍怎么选', '你能做什么'],
            messageSeed: 0,
            sessionId: ''
        };
    },
    methods: {
        togglePanel() {
            this.open = !this.open;
            if (this.open) {
                this.$nextTick(() => {
                    this.scrollToBottom();
                    if (this.$refs.input) {
                        this.$refs.input.focus();
                    }
                });
            }
        },
        resetChat() {
            this.messages = [];
            this.suggestions = ['200元以内篮球装备', '查一下我的订单', '羽毛球拍怎么选', '你能做什么'];
            this.draft = '';
            if (this.$refs.input) {
                this.$refs.input.focus();
            }
        },
        askSuggestion(text) {
            this.draft = text;
            this.sendMessage();
        },
        handleKeydown(event) {
            if (event.key === 'Enter' && !event.shiftKey && !event.isComposing) {
                event.preventDefault();
                this.sendMessage();
            }
        },
        sendMessage() {
            var text = this.draft.trim();
            if (!text || this.loading) {
                return;
            }
            var history = this.getConversationHistory();
            this.messages.push({
                id: ++this.messageSeed,
                role: 'user',
                text: text
            });
            this.draft = '';
            this.loading = true;
            this.scrollToBottom();

            axios({
                method: 'POST',
                url: '/agent/chat',
                data: {
                    message: text,
                    mode: 'customer',
                    sessionId: this.getSessionId(),
                    history: history
                }
            }).then(result => {
                var body = result.data || {};
                if (body.state !== 66) {
                    this.pushError(body.message || '智能助手暂时没有响应');
                    return;
                }
                var data = body.data || {};
                this.sessionId = data.sessionId || this.sessionId;
                if (this.sessionId) {
                    sessionStorage.setItem('yd_agent_session', this.sessionId);
                }
                this.messages.push({
                    id: ++this.messageSeed,
                    role: 'assistant',
                    text: data.answer || '我已经完成这次查询。',
                    response: data,
                    confirming: false,
                    confirmationStatus: ''
                });
                this.suggestions = Array.isArray(data.suggestions) && data.suggestions.length
                    ? data.suggestions : [];
            }).catch(() => {
                this.pushError('网络连接不稳定，请稍后再试；商城原有购物流程不受影响');
            }).finally(() => {
                this.loading = false;
                this.scrollToBottom();
            });
        },
        confirmAction(message) {
            if (!message.response || !message.response.confirmation || message.confirming) {
                return;
            }
            message.confirming = true;
            this.scrollToBottom();
            axios({
                method: 'POST',
                url: '/agent/confirm',
                data: {
                    token: message.response.confirmation.token,
                    mode: 'customer'
                }
            }).then(result => {
                var body = result.data || {};
                if (body.state !== 66) {
                    this.pushError(body.message || '确认执行失败，请重新发起操作');
                    message.confirmationStatus = 'failed';
                    return;
                }
                var data = body.data || {};
                message.confirmationStatus = 'done';
                this.messages.push({
                    id: ++this.messageSeed,
                    role: 'assistant',
                    text: data.answer || '操作已执行。',
                    response: data
                });
            }).catch(() => {
                this.pushError('确认执行失败，请检查网络后重试');
                message.confirmationStatus = 'failed';
            }).finally(() => {
                message.confirming = false;
                this.scrollToBottom();
            });
        },
        pushError(text) {
            this.messages.push({
                id: ++this.messageSeed,
                role: 'assistant',
                text: text,
                response: null
            });
            this.scrollToBottom();
        },
        getSessionId() {
            if (this.sessionId) {
                return this.sessionId;
            }
            var saved = sessionStorage.getItem('yd_agent_session');
            if (saved) {
                this.sessionId = saved;
                return saved;
            }
            var random = Math.random().toString(36).slice(2, 12);
            this.sessionId = 'ags_' + Date.now().toString(36) + random;
            sessionStorage.setItem('yd_agent_session', this.sessionId);
            return this.sessionId;
        },
        getConversationHistory() {
            return this.messages
                .filter(message => message.text && (message.role === 'user' || message.role === 'assistant'))
                .slice(-8)
                .map(message => ({
                    role: message.role,
                    text: String(message.text).slice(0, 800)
                }));
        },
        needsLogin(message) {
            return message.response && message.response.error
                && message.text.indexOf('登录') >= 0;
        },
        isScalar(value) {
            return value === null || typeof value !== 'object';
        },
        metricLabel(key) {
            var labels = {
                productCount: '在售商品',
                lowStockCount: '低库存商品',
                userCount: '校园会员',
                orderCount: '累计订单',
                pendingShipmentCount: '待发货',
                revenue: '累计成交'
            };
            return labels[key] || key;
        },
        metricValue(key, value) {
            if (key === 'revenue') {
                return '¥' + Number(value || 0).toLocaleString('zh-CN');
            }
            return Number(value || 0).toLocaleString('zh-CN');
        },
        riskText(level) {
            if (level === 'write_high') {
                return '高风险操作';
            }
            if (level === 'write_low') {
                return '写操作';
            }
            return '只读确认';
        },
        shortTrace(traceId) {
            return traceId ? traceId.slice(-12).toUpperCase() : '';
        },
        scrollToBottom() {
            this.$nextTick(() => {
                var box = this.$refs.messages;
                if (box) {
                    box.scrollTop = box.scrollHeight;
                }
            });
        }
    },
    mounted() {
        this.sessionId = sessionStorage.getItem('yd_agent_session') || '';
        var params = new URLSearchParams(window.location.search);
        if (params.get('agent') === '1') {
            this.open = true;
            this.$nextTick(() => {
                this.scrollToBottom();
                if (this.$refs.input) {
                    this.$refs.input.focus();
                }
            });
        }
    }
};
</script>

<style scoped>
.yd-agent {
    --yd-ink: #111827;
    --yd-ink-soft: #1c283b;
    --yd-lime: #c9f25d;
    --yd-orange: #ff6b3d;
    --yd-paper: #f7f8f2;
    --yd-muted: #76808d;
    font-family: "Microsoft YaHei", "PingFang SC", "Noto Sans SC", Arial, sans-serif;
    position: fixed;
    right: 22px;
    bottom: 22px;
    z-index: 2200;
}

.yd-agent,
.yd-agent * {
    box-sizing: border-box;
}

.yd-agent button,
.yd-agent textarea {
    font: inherit;
}

.yd-agent__launcher {
    align-items: center;
    background: var(--yd-ink);
    border: 1px solid rgba(255, 255, 255, 0.14);
    border-radius: 999px;
    bottom: 0;
    box-shadow: 0 18px 46px rgba(17, 24, 39, 0.26);
    color: #fff;
    cursor: pointer;
    display: flex;
    gap: 11px;
    height: 62px;
    justify-content: center;
    min-width: 62px;
    padding: 8px 19px 8px 9px;
    position: absolute;
    right: 0;
    transition: transform 0.22s ease, box-shadow 0.22s ease;
}

.yd-agent__launcher:hover {
    box-shadow: 0 22px 54px rgba(17, 24, 39, 0.32);
    transform: translateY(-3px);
}

.yd-agent__launcher-ball {
    background: var(--yd-lime);
    border: 2px solid rgba(17, 24, 39, 0.82);
    border-radius: 50%;
    display: block;
    flex: 0 0 auto;
    height: 42px;
    overflow: hidden;
    position: relative;
    transform: rotate(-12deg);
    width: 42px;
}

.yd-agent__launcher-ball::before,
.yd-agent__launcher-ball::after {
    border: 1.5px solid var(--yd-ink);
    border-radius: 50%;
    content: "";
    inset: 6px;
    position: absolute;
}

.yd-agent__launcher-ball::after {
    border-color: transparent var(--yd-ink);
    border-radius: 0;
    inset: 2px 19px;
}

.yd-agent__launcher-ball i {
    background: var(--yd-orange);
    border-radius: 50%;
    height: 6px;
    position: absolute;
    right: 4px;
    top: 5px;
    width: 6px;
}

.yd-agent__launcher-ball i:nth-child(2) {
    background: var(--yd-ink);
    height: 3px;
    left: 4px;
    right: auto;
    top: 25px;
    width: 3px;
}

.yd-agent__launcher-ball i:nth-child(3) {
    background: var(--yd-ink);
    bottom: 4px;
    left: 20px;
    right: auto;
    top: auto;
    width: 3px;
    height: 3px;
}

.yd-agent__launcher-copy {
    align-items: flex-start;
    display: flex;
    flex-direction: column;
    line-height: 1;
    text-align: left;
}

.yd-agent__launcher-copy strong {
    color: #fff;
    font-size: 14px;
    font-weight: 800;
    letter-spacing: -0.03em;
}

.yd-agent__launcher-copy small {
    color: rgba(255, 255, 255, 0.44);
    font-size: 7px;
    font-weight: 700;
    letter-spacing: 0.1em;
    margin-top: 7px;
    white-space: nowrap;
}

.yd-agent--open .yd-agent__launcher {
    bottom: 12px;
    height: 42px;
    min-width: 42px;
    padding: 0;
    right: 12px;
    width: 42px;
    z-index: 2;
}

.yd-agent--open .yd-agent__launcher-ball {
    display: none;
}

.yd-agent__launcher-close {
    color: #fff;
    font-size: 25px;
    font-weight: 300;
    line-height: 1;
}

.yd-agent__panel {
    background: #fff;
    border: 1px solid rgba(17, 24, 39, 0.11);
    border-radius: 26px;
    bottom: 0;
    box-shadow: 0 30px 90px rgba(17, 24, 39, 0.24);
    display: flex;
    flex-direction: column;
    max-height: min(760px, calc(100vh - 44px));
    overflow: hidden;
    position: absolute;
    right: 0;
    width: min(430px, calc(100vw - 24px));
}

.yd-agent__header {
    align-items: center;
    background:
        radial-gradient(circle at 84% 20%, rgba(201, 242, 93, 0.18), transparent 25%),
        linear-gradient(135deg, var(--yd-ink), #1c2b42);
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
    display: flex;
    flex: 0 0 auto;
    justify-content: space-between;
    min-height: 86px;
    padding: 16px 64px 16px 18px;
    position: relative;
}

.yd-agent__header::after {
    background:
        linear-gradient(90deg, rgba(255, 255, 255, 0.04) 1px, transparent 1px),
        linear-gradient(rgba(255, 255, 255, 0.04) 1px, transparent 1px);
    background-size: 25px 25px;
    content: "";
    inset: 0;
    mask-image: linear-gradient(90deg, transparent 30%, #000);
    pointer-events: none;
    position: absolute;
}

.yd-agent__identity {
    align-items: center;
    display: flex;
    gap: 12px;
    position: relative;
    z-index: 1;
}

.yd-agent__avatar {
    align-items: center;
    background: var(--yd-lime);
    border-radius: 15px;
    display: flex;
    height: 47px;
    justify-content: center;
    position: relative;
    transform: rotate(-3deg);
    width: 47px;
}

.yd-agent__avatar::before,
.yd-agent__avatar::after {
    border: 1.5px solid var(--yd-ink);
    border-radius: 50%;
    content: "";
    height: 28px;
    position: absolute;
    width: 28px;
}

.yd-agent__avatar::after {
    border-color: transparent var(--yd-ink);
    border-radius: 0;
    height: 31px;
    width: 12px;
}

.yd-agent__avatar span {
    background: var(--yd-orange);
    border: 2px solid var(--yd-ink);
    border-radius: 50%;
    height: 11px;
    position: absolute;
    right: 4px;
    top: 4px;
    width: 11px;
    z-index: 2;
}

.yd-agent__identity-copy {
    display: flex;
    flex-direction: column;
}

.yd-agent__identity-copy small {
    color: var(--yd-lime);
    font-size: 7px;
    font-style: normal;
    font-weight: 800;
    letter-spacing: 0.16em;
    margin-bottom: 4px;
}

.yd-agent__identity-copy strong {
    color: #fff;
    font-size: 16px;
    font-weight: 800;
    letter-spacing: -0.04em;
}

.yd-agent__identity-copy em {
    align-items: center;
    color: rgba(255, 255, 255, 0.44);
    display: flex;
    font-size: 8px;
    font-style: normal;
    gap: 5px;
    margin-top: 5px;
}

.yd-agent__identity-copy em i {
    background: #73df7b;
    border-radius: 50%;
    box-shadow: 0 0 0 3px rgba(115, 223, 123, 0.1);
    height: 5px;
    width: 5px;
}

.yd-agent__header-action {
    align-items: center;
    background: rgba(255, 255, 255, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    color: rgba(255, 255, 255, 0.7);
    cursor: pointer;
    display: flex;
    font-size: 17px;
    height: 32px;
    justify-content: center;
    position: absolute;
    right: 17px;
    top: 16px;
    width: 32px;
    z-index: 2;
}

.yd-agent__header-action:hover {
    background: var(--yd-lime);
    color: var(--yd-ink);
}

.yd-agent__messages {
    background:
        radial-gradient(circle at 5% 0, rgba(201, 242, 93, 0.1), transparent 20%),
        #f7f8f2;
    flex: 1 1 auto;
    min-height: 240px;
    overflow-y: auto;
    overscroll-behavior: contain;
    padding: 18px;
    scrollbar-color: #c6ccbf transparent;
    scrollbar-width: thin;
}

.yd-agent__welcome {
    padding: 5px 2px 4px;
}

.yd-agent__court {
    background:
        radial-gradient(circle at 82% 24%, rgba(201, 242, 93, 0.22), transparent 18%),
        linear-gradient(122deg, var(--yd-ink), #1c2a40);
    border-radius: 20px;
    color: #fff;
    height: 126px;
    margin-bottom: 22px;
    overflow: hidden;
    position: relative;
}

.yd-agent__court::before {
    border: 1px solid rgba(255, 255, 255, 0.13);
    border-radius: 80px 80px 12px 12px;
    content: "";
    inset: 12px 18px;
    position: absolute;
}

.yd-agent__court::after {
    border-left: 1px solid rgba(255, 255, 255, 0.13);
    content: "";
    inset: 12px auto 12px 48%;
    position: absolute;
}

.yd-agent__court-ball {
    background: var(--yd-lime);
    border: 2px solid rgba(17, 24, 39, 0.7);
    border-radius: 50%;
    box-shadow: 0 16px 32px rgba(0, 0, 0, 0.25);
    height: 54px;
    position: absolute;
    right: 38px;
    top: 31px;
    transform: rotate(-15deg);
    width: 54px;
    z-index: 2;
}

.yd-agent__court-ball::before,
.yd-agent__court-ball::after {
    border: 1.4px solid rgba(17, 24, 39, 0.66);
    border-radius: 50%;
    content: "";
    inset: 7px;
    position: absolute;
}

.yd-agent__court-ball::after {
    border-color: transparent rgba(17, 24, 39, 0.66);
    border-radius: 0;
    inset: 3px 25px;
}

.yd-agent__court-line {
    background: rgba(255, 255, 255, 0.08);
    height: 100%;
    left: 48%;
    position: absolute;
    top: 0;
    width: 1px;
}

.yd-agent__court-label {
    bottom: 16px;
    color: rgba(255, 255, 255, 0.36);
    font-size: 7px;
    font-weight: 800;
    left: 30px;
    letter-spacing: 0.18em;
    position: absolute;
}

.yd-agent__eyebrow {
    color: #727d00;
    font-size: 9px;
    font-weight: 800;
    letter-spacing: 0.13em;
    margin: 0 0 8px;
    text-transform: uppercase;
}

.yd-agent__welcome h2 {
    color: var(--yd-ink);
    font-size: 24px;
    font-weight: 900;
    letter-spacing: -0.055em;
    line-height: 1.22;
    margin: 0 0 10px;
}

.yd-agent__intro {
    color: #727b88;
    font-size: 11px;
    line-height: 1.8;
    margin: 0;
}

.yd-agent__features {
    border-top: 1px solid rgba(17, 24, 39, 0.08);
    display: grid;
    gap: 7px;
    grid-template-columns: repeat(3, 1fr);
    margin-top: 18px;
    padding-top: 14px;
}

.yd-agent__features span {
    color: #606977;
    font-size: 9px;
    white-space: nowrap;
}

.yd-agent__features i {
    color: var(--yd-orange);
    font-size: 7px;
    font-style: normal;
    font-weight: 800;
    margin-right: 4px;
}

.yd-agent__message {
    margin-bottom: 16px;
}

.yd-agent__message:last-child {
    margin-bottom: 0;
}

.yd-agent__message--user {
    display: flex;
    justify-content: flex-end;
}

.yd-agent__user-bubble {
    background: var(--yd-ink);
    border-radius: 15px 15px 4px 15px;
    color: #fff;
    font-size: 11px;
    line-height: 1.65;
    max-width: 82%;
    padding: 10px 13px;
}

.yd-agent__agent-row {
    align-items: flex-start;
    display: flex;
    gap: 8px;
}

.yd-agent__mini-avatar {
    align-items: center;
    background: var(--yd-lime);
    border: 1px solid rgba(17, 24, 39, 0.18);
    border-radius: 50%;
    color: var(--yd-ink);
    display: flex;
    flex: 0 0 auto;
    font-size: 9px;
    font-weight: 900;
    height: 26px;
    justify-content: center;
    margin-top: 2px;
    width: 26px;
}

.yd-agent__answer {
    background: #fff;
    border: 1px solid rgba(17, 24, 39, 0.08);
    border-radius: 5px 15px 15px 15px;
    box-shadow: 0 10px 30px rgba(17, 24, 39, 0.055);
    min-width: 0;
    padding: 12px 13px;
    width: calc(100% - 34px);
}

.yd-agent__answer > p {
    color: #323b48;
    font-size: 11px;
    line-height: 1.75;
    margin: 0;
    white-space: pre-line;
}

.yd-agent__steps {
    border-top: 1px dashed rgba(17, 24, 39, 0.09);
    display: flex;
    flex-wrap: wrap;
    gap: 8px 12px;
    margin-top: 10px;
    padding-top: 9px;
}

.yd-agent__steps span {
    align-items: center;
    color: #8c95a0;
    display: flex;
    font-size: 8px;
    gap: 5px;
}

.yd-agent__steps span i {
    background: var(--yd-lime-deep, #a9dc2f);
    border-radius: 50%;
    height: 5px;
    width: 5px;
}

.yd-agent__login {
    align-items: center;
    background: #fff8f3;
    border: 1px solid rgba(255, 107, 61, 0.17);
    border-radius: 10px;
    display: flex;
    gap: 10px;
    justify-content: space-between;
    margin-top: 10px;
    padding: 8px 10px;
}

.yd-agent__login p {
    color: #7e6257;
    font-size: 9px;
    margin: 0;
}

.yd-agent__login a {
    color: var(--yd-orange);
    flex: 0 0 auto;
    font-size: 9px;
    font-weight: 800;
}

.yd-agent__products {
    display: grid;
    gap: 7px;
    margin-top: 11px;
}

.yd-agent__product {
    align-items: center;
    background: #f7f8f3;
    border: 1px solid rgba(17, 24, 39, 0.065);
    border-radius: 11px;
    display: flex;
    gap: 9px;
    min-width: 0;
    padding: 7px;
    position: relative;
    transition: border-color 0.2s ease, transform 0.2s ease;
}

.yd-agent__product:hover {
    border-color: rgba(169, 220, 47, 0.72);
    transform: translateY(-1px);
}

.yd-agent__product-image {
    flex: 0 0 auto;
    height: 54px;
    position: relative;
    width: 54px;
}

.yd-agent__product-image img {
    background: #fff;
    border-radius: 8px;
    height: 100%;
    object-fit: cover;
    width: 100%;
}

.yd-agent__product-image i {
    background: var(--yd-lime);
    border-radius: 99px;
    bottom: -3px;
    color: var(--yd-ink);
    font-size: 6px;
    font-style: normal;
    font-weight: 800;
    padding: 2px 5px;
    position: absolute;
    right: -3px;
}

.yd-agent__product-image i.is-empty {
    background: var(--yd-orange);
    color: #fff;
}

.yd-agent__product-copy {
    display: flex;
    flex: 1 1 auto;
    flex-direction: column;
    min-width: 0;
}

.yd-agent__product-copy > small {
    color: #9aa1aa;
    font-size: 7px;
    margin-bottom: 2px;
}

.yd-agent__product-copy > strong {
    color: var(--yd-ink);
    display: block;
    font-size: 9px;
    font-weight: 800;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.yd-agent__product-copy > span {
    align-items: baseline;
    display: flex;
    gap: 8px;
    margin-top: 5px;
}

.yd-agent__product-copy b {
    color: var(--yd-orange);
    font-size: 10px;
}

.yd-agent__product-copy em {
    color: #9aa1aa;
    font-size: 7px;
    font-style: normal;
}

.yd-agent__product-arrow {
    color: #b2b8c0;
    font-size: 10px;
    font-style: normal;
    padding: 0 2px;
}

.yd-agent__records {
    display: grid;
    gap: 6px;
    margin-top: 10px;
}

.yd-agent__record {
    align-items: center;
    background: #f7f8f3;
    border-radius: 9px;
    display: flex;
    gap: 8px;
    justify-content: space-between;
    padding: 8px 9px;
}

.yd-agent__record > span {
    display: flex;
    flex-direction: column;
    min-width: 0;
}

.yd-agent__record small {
    color: #9aa1aa;
    font-size: 7px;
    margin-bottom: 2px;
}

.yd-agent__record strong {
    color: #343d49;
    font-size: 8px;
    font-weight: 700;
    max-width: 175px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.yd-agent__record-meta {
    align-items: flex-end;
    flex: 0 0 auto;
}

.yd-agent__record-meta b {
    color: var(--yd-orange);
    font-size: 9px;
}

.yd-agent__record-meta em {
    background: #e9f6c6;
    border-radius: 99px;
    color: #5c6900;
    font-size: 7px;
    font-style: normal;
    margin-top: 3px;
    padding: 2px 5px;
}

.yd-agent__metrics {
    display: grid;
    gap: 6px;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    margin-top: 10px;
}

.yd-agent__metric {
    background: #f7f8f3;
    border-radius: 9px;
    padding: 8px 9px;
}

.yd-agent__metric small {
    color: #929aa4;
    display: block;
    font-size: 7px;
    margin-bottom: 3px;
}

.yd-agent__metric strong {
    color: var(--yd-ink);
    font-size: 13px;
    font-weight: 900;
    letter-spacing: -0.03em;
}

.yd-agent__confirm {
    background:
        linear-gradient(120deg, rgba(201, 242, 93, 0.12), transparent 65%),
        #111827;
    border-radius: 13px;
    color: #fff;
    margin-top: 11px;
    overflow: hidden;
    padding: 12px;
    position: relative;
}

.yd-agent__confirm::after {
    border: 1px solid rgba(201, 242, 93, 0.18);
    border-radius: 50%;
    content: "";
    height: 90px;
    position: absolute;
    right: -45px;
    top: -46px;
    width: 90px;
}

.yd-agent__confirm.is-done {
    opacity: 0.76;
}

.yd-agent__confirm-head {
    align-items: center;
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    position: relative;
    z-index: 1;
}

.yd-agent__confirm-head span {
    color: var(--yd-lime);
    font-size: 8px;
    font-weight: 800;
    letter-spacing: 0.08em;
}

.yd-agent__confirm-head i {
    background: rgba(255, 107, 61, 0.16);
    border: 1px solid rgba(255, 107, 61, 0.3);
    border-radius: 99px;
    color: #ff9b7d;
    font-size: 6px;
    font-style: normal;
    padding: 3px 6px;
}

.yd-agent__confirm > strong {
    color: #fff;
    display: block;
    font-size: 11px;
    margin-bottom: 5px;
    position: relative;
    z-index: 1;
}

.yd-agent__confirm > p {
    color: rgba(255, 255, 255, 0.55);
    font-size: 8px;
    line-height: 1.7;
    margin: 0;
    position: relative;
    z-index: 1;
}

.yd-agent__confirm-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 9px;
    position: relative;
    z-index: 1;
}

.yd-agent__confirm-meta span {
    color: rgba(255, 255, 255, 0.32);
    font-size: 6px;
}

.yd-agent__confirm-meta span::before {
    color: var(--yd-lime);
    content: "· ";
}

.yd-agent__confirm > button {
    align-items: center;
    background: var(--yd-lime);
    border: 0;
    border-radius: 8px;
    color: var(--yd-ink);
    cursor: pointer;
    display: flex;
    font-size: 9px;
    font-weight: 800;
    justify-content: space-between;
    margin-top: 11px;
    padding: 9px 11px;
    position: relative;
    width: 100%;
    z-index: 1;
}

.yd-agent__confirm > button:hover:not(:disabled) {
    background: #dcff78;
}

.yd-agent__confirm > button:disabled {
    cursor: wait;
    opacity: 0.65;
}

.yd-agent__confirm-success {
    align-items: center;
    background: rgba(201, 242, 93, 0.1);
    border-radius: 8px;
    color: var(--yd-lime);
    display: flex;
    font-size: 8px;
    gap: 6px;
    margin-top: 10px;
    padding: 8px;
    position: relative;
    z-index: 1;
}

.yd-agent__trace {
    color: #b0b6bf;
    font-size: 6px;
    letter-spacing: 0.09em;
    margin-top: 8px;
    text-align: right;
}

.yd-agent__thinking {
    align-items: center;
    background: #fff;
    border: 1px solid rgba(17, 24, 39, 0.08);
    border-radius: 5px 15px 15px 15px;
    display: flex;
    gap: 4px;
    padding: 12px 13px;
}

.yd-agent__thinking > span {
    animation: ydAgentBounce 1.2s ease-in-out infinite;
    background: var(--yd-lime-deep, #a9dc2f);
    border-radius: 50%;
    height: 5px;
    width: 5px;
}

.yd-agent__thinking > span:nth-child(2) {
    animation-delay: 0.12s;
}

.yd-agent__thinking > span:nth-child(3) {
    animation-delay: 0.24s;
}

.yd-agent__thinking em {
    color: #949ca6;
    font-size: 8px;
    font-style: normal;
    margin-left: 4px;
}

.yd-agent__suggestions {
    background: #fff;
    border-top: 1px solid rgba(17, 24, 39, 0.07);
    display: flex;
    flex: 0 0 auto;
    gap: 6px;
    overflow-x: auto;
    padding: 10px 14px;
    scrollbar-width: none;
}

.yd-agent__suggestions::-webkit-scrollbar {
    display: none;
}

.yd-agent__suggestions button {
    background: #f3f5ec;
    border: 1px solid rgba(17, 24, 39, 0.06);
    border-radius: 99px;
    color: #606a76;
    cursor: pointer;
    flex: 0 0 auto;
    font-size: 8px;
    padding: 6px 10px;
    transition: 0.18s ease;
}

.yd-agent__suggestions button:hover {
    background: #ebf8c9;
    border-color: rgba(169, 220, 47, 0.55);
    color: var(--yd-ink);
}

.yd-agent__composer {
    background: #fff;
    border-top: 1px solid rgba(17, 24, 39, 0.08);
    flex: 0 0 auto;
    padding: 12px 14px 10px;
}

.yd-agent__composer > label {
    color: #8e96a0;
    display: block;
    font-size: 7px;
    font-weight: 700;
    letter-spacing: 0.08em;
    margin-bottom: 6px;
    text-transform: uppercase;
}

.yd-agent__composer-row {
    align-items: flex-end;
    background: #f4f6ef;
    border: 1px solid transparent;
    border-radius: 13px;
    display: flex;
    gap: 7px;
    padding: 7px 7px 7px 11px;
    transition: 0.2s ease;
}

.yd-agent__composer-row:focus-within {
    background: #fff;
    border-color: rgba(169, 220, 47, 0.8);
    box-shadow: 0 0 0 3px rgba(201, 242, 93, 0.18);
}

.yd-agent__composer textarea {
    background: transparent;
    border: 0;
    color: var(--yd-ink);
    flex: 1 1 auto;
    font-size: 10px;
    line-height: 1.5;
    max-height: 82px;
    min-height: 24px;
    outline: 0;
    padding: 4px 0;
    resize: none;
    width: 100%;
}

.yd-agent__composer textarea::placeholder {
    color: #a6adb6;
}

.yd-agent__composer-row button {
    align-items: center;
    background: var(--yd-ink);
    border: 0;
    border-radius: 9px;
    color: #fff;
    cursor: pointer;
    display: flex;
    flex: 0 0 auto;
    font-size: 8px;
    font-weight: 800;
    gap: 6px;
    height: 31px;
    padding: 0 9px 0 11px;
}

.yd-agent__composer-row button i {
    align-items: center;
    background: var(--yd-lime);
    border-radius: 5px;
    color: var(--yd-ink);
    display: flex;
    font-size: 11px;
    font-style: normal;
    height: 18px;
    justify-content: center;
    width: 18px;
}

.yd-agent__composer-row button:disabled {
    cursor: not-allowed;
    opacity: 0.42;
}

.yd-agent__composer > p {
    color: #b2b8bf;
    font-size: 6px;
    margin: 7px 1px 0;
    text-align: center;
}

@keyframes ydAgentBounce {
    0%,
    60%,
    100% {
        opacity: 0.35;
        transform: translateY(0);
    }
    30% {
        opacity: 1;
        transform: translateY(-4px);
    }
}

@media (max-width: 560px) {
    .yd-agent {
        bottom: 14px;
        right: 12px;
    }

    .yd-agent__launcher {
        height: 54px;
        padding: 6px 15px 6px 7px;
    }

    .yd-agent__launcher-ball {
        height: 40px;
        width: 40px;
    }

    .yd-agent__launcher-copy small {
        display: none;
    }

    .yd-agent__panel {
        border-radius: 20px;
        bottom: -6px;
        height: calc(100vh - 16px);
        max-height: none;
        right: -6px;
        width: calc(100vw - 12px);
    }

    .yd-agent__header {
        min-height: 80px;
        padding-left: 15px;
    }

    .yd-agent__messages {
        padding: 14px;
    }

    .yd-agent__welcome h2 {
        font-size: 22px;
    }
}

@media (prefers-reduced-motion: reduce) {
    .yd-agent *,
    .yd-agent *::before,
    .yd-agent *::after {
        animation: none !important;
        scroll-behavior: auto !important;
        transition-duration: 0.01ms !important;
    }
}
</style>
