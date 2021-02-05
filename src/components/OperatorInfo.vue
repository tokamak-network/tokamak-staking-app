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
                        <image :source=TokamakIcon :style="{width: windowWidth*0.097, height: windowHeight*0.028, resizeMode: 'contain'}"></image>
                        <text class="modtal-top-title">{{operator.name}}</text>
                        <touchable-opacity :on-press="()=>close()">
                        <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain', marginLeft: '75%'}"></image>
                        </touchable-opacity>
                    </view>
                <view class="divider" />
                <view>
                    <operator-info-sub title='Website' :content="operator.website"></operator-info-sub>
                    <operator-info-sub title='Description' :content="operator.description"></operator-info-sub>
                    <operator-info-sub title='Operator Address' :content="operator.address"></operator-info-sub>
                    <operator-info-sub title='Operator Contract' :content="operator.layer2"></operator-info-sub>
                    <operator-info-sub title='Chain ID' :content="operator.chainId"></operator-info-sub>
                    <operator-info-sub title='Commit Count' :content="operator.finalizeCount"></operator-info-sub>
                    <operator-info-sub title='Recent Commit' :content="fromNow(operator.lastFinalizedAt)"></operator-info-sub>
                    <operator-info-sub title='Running Time' :content="fromNow(operator.deployedAt, true)"></operator-info-sub>
                    <operator-info-sub title='Commission Rate' :content="`${operator.isCommissionRateNegative === 1 ? '-' : ''}${rateOf(operator.commissionRate)}`"></operator-info-sub>
                    <operator-info-sub title='Reward' :content="currencyAmount(operator.userReward)"></operator-info-sub>
                    <operator-info-sub title='Total Staked' :content="currencyAmount(operator.totalStaked)"></operator-info-sub>
                    <operator-info-sub title='Not Withdrawable' :content="currencyAmount(operator.userNotWithdrawable)"></operator-info-sub>
                    <operator-info-sub title='Withdrawable' :content="currencyAmount(operator.userWithdrawable)"></operator-info-sub>
                    <operator-info-sub title='New Commission Rate' :content="`${operator.delayedCommissionRateNegative === 1? '-' : ''}${rateOf(operator.delayedCommissionRate)}`"></operator-info-sub>
                    <operator-info-sub title='New Commission Rate Changed At' :content="operator.delayedCommissionBlock"></operator-info-sub>
                    <operator-info-sub title='Withdrawal Delay' :content="`${delay()}${' blocks'}`"></operator-info-sub>
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
import TokamakIcon from "../../assets/TokamakLogo.png";
import CloseIcon from "../../assets/icon-close.png";
import { mapState, mapGetters } from "vuex";

export default {
    data() {
        return{
            TokamakIcon,
            CloseIcon,
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
        "operator-info-sub": OperatorInfoSub
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
        sendPropToParent() {
            this.$emit('propFromChild', this.modalVisible)
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
    border-radius: 10px;
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
</style>
