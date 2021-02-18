<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background">
        <view class="modal-container" :style="{height: windowHeight*0.91, width: windowWidth, top: windowHeight * 0.09}">
            <scroll-view :showsVerticalScrollIndicator="false">
                <view class="modal-content">
                    <view class="modal-top">
                        <touchable-opacity :on-press="seenWeb === true ? ()=>closeWebView() : null">
                        <image :source="seenWeb === true ? BackIcon : TokamakIcon" :style="{width: windowWidth*0.097, height: windowHeight*0.028, resizeMode: 'contain'}">                        
                        </image>
                        </touchable-opacity>
                        <text v-if="!seenWeb" class="modtal-top-title">{{operator.name}}</text>
                        <touchable-opacity :style="{marginLeft: 'auto'}" :on-press="()=>close()">
                        <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain'}"></image>
                        </touchable-opacity>
                    </view>
                    <view class="divider" />
                    <view v-if="!seenWeb">
                        <operator-info-sub title='Website' :content="operator.website" :seenWeb="seenWeb" @propFromChild="childPropReceived"></operator-info-sub>
                        <operator-info-sub title='Description' :content="operator.description"></operator-info-sub>
                        <operator-info-sub title='Operator Address' :content="operator.address" @propFromChild="childPropReceived"></operator-info-sub>
                        <operator-info-sub title='Operator Contract' :content="operator.layer2" @propFromChild="childPropReceived"></operator-info-sub>
                        <operator-info-sub title='Chain ID' :content="operator.chainId"></operator-info-sub>
                        <operator-info-sub title='Commit Count' :content="operator.finalizeCount"></operator-info-sub>
                        <operator-info-sub title='Recent Commit' :content="fromNow(operator.lastFinalizedAt)"></operator-info-sub>
                        <operator-info-sub title='Running Time' :content="fromNow(operator.deployedAt, true)"></operator-info-sub>
                        <operator-info-sub title='Commission Rate' :content="`${operator.isCommissionRateNegative === '1' ? '-' : ''}${rateOf(operator.commissionRate)}`"></operator-info-sub>
                        <operator-info-sub title='Reward' :content="currencyAmount(operator.userReward)"></operator-info-sub>
                        <operator-info-sub title='Total Staked' :content="currencyAmount(operator.totalStaked)"></operator-info-sub>
                        <operator-info-sub title='Not Withdrawable' :content="currencyAmount(operator.userNotWithdrawable)"></operator-info-sub>
                        <operator-info-sub title='Withdrawable' :content="currencyAmount(operator.userWithdrawable)"></operator-info-sub>
                        <operator-info-sub title='New Commission Rate' :content="`${operator.delayedCommissionRateNegative === 1? '-' : ''}${rateOf(operator.delayedCommissionRate)}`"></operator-info-sub>
                        <operator-info-sub title='New Commission Rate Changed At' :content="operator.delayedCommissionBlock"></operator-info-sub>
                        <operator-info-sub title='Withdrawal Delay' :content="`${delay()}${' blocks'}`"></operator-info-sub> 
                    </view>
                    <view v-else class="web-view-container" :style="{width: windowWidth, height: windowHeight*0.81}">
                        <web-view :url="uri"></web-view>
                    </view>
                </view>
            </scroll-view>
        </view> 
    </view>
    </Modal>
</template>

<script>
import {
  Dimensions,
  Modal
} from "react-native";
import ButtonMain from "@/components/ButtonMain"
import OperatorInfoSub from "@/components/OperatorInfoSub"
import WebViewComponent from "@/components/WebViewComponent"
import TokamakIcon from "../../assets/TokamakLogo.png";
import CloseIcon from "../../assets/icon-close.png";
import BackIcon from "../../assets/back-arrow.png";
import { mapState, mapGetters } from "vuex";

export default {
    data() {
        return{
            TokamakIcon,
            CloseIcon,
            BackIcon,
            seenWeb: false,
            uri: ''
        }
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        },
        layer2: {
      required: true,
      type: String,
    },
    },
    components: {
        "button-main": ButtonMain,
        "operator-info-sub": OperatorInfoSub,
        "web-view": WebViewComponent
    },
    computed: {
         ...mapState([
      "DepositManager",
      "tonBalance",
      "user",
      "TON",
      "WTON",
      "SeigManager",
      "selectedOperator"
    ]),
    ...mapGetters(["operatorByLayer2"]),
    operator() {
      return this.operatorByLayer2(this.layer2);
    },
    fromNow() {
      return (timestamp, suffix = false) =>
        this.$options.filters.fromNow(timestamp, suffix);
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
    rateOf() {
      return (commissionRate) => this.$options.filters.rateOf(commissionRate);
    },
        windowWidth () {
            return Dimensions.get('window').width
        },
        windowHeight () {
            return Dimensions.get('window').height
        },
    },
    methods: {
        close() {
            this.modalVisible = false
            this.$emit('propFromChild', this.modalVisible)
        },
        closeWebView() {
            this.seenWeb = false
        },
        sendPropToParent() {
            this.$emit('propFromChild', this.modalVisible)
        },
        childPropReceived(args1, args2) {
            this.seenWeb = args1
            this.uri = args2
        },
        delay () {
            const operatorDelay = this.operator.withdrawalDelay;
            const globalDelay = this.operator.globalWithdrawalDelay;
            if(operatorDelay > globalDelay) {
                return Number(operatorDelay);
            }
            else {
                return Number(globalDelay);
            }
        },
    }
}
</script>

<style>
.modal-background {
   background-color:rgba( 0, 0, 0, 0.45 );
}
.modal-container {
    background-color: #FFFFFF;
    justify-content: center;
    border-width: 1px;
    border-color: #ffffff;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
}
.modal-content {
    display: flex;
    flex-direction: column;
    padding-left: 7.5%;
    padding-right: 5.6%;
}
.modal-top {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-top: 4%;
    margin-bottom: 4%;
}
.modtal-top-title {
    margin-left: 5%;
    font-size: 20;
    font-weight: bold;
    color: #131315;
}
.divider {
  width: 100%;
  height: 1px;
  background-color: #dfe4ee;
  margin-bottom: 5.8%;
}
.web-view-container {
    left: -7.5%;
    top: -2.6%;
}
</style>
