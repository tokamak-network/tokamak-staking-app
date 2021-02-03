<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
        <view class="modal-container" :style="{height: windowHeight*0.955, width: windowWidth}">
            <scroll-view>
                <view class="modal-content">
                    <view class="modal-top">
                        <image :source=TokamakIcon :style="{width: windowWidth*0.097, height: windowHeight*0.028, resizeMode: 'contain'}"></image>
                        <text class="modtal-top-title">Tokamak1</text>
                        <touchable-opacity :on-press="()=>close()">
                        <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain', marginLeft: 'auto'}"></image>
                        </touchable-opacity>
                    </view>
                <view class="divider" />
                <view>
                    <operator-info-sub v-for="(value, key) in dummyData" :key="key" :title=key :content=value></operator-info-sub>
                </view>
                </view>

            </scroll-view>
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

export default {
    data() {
        return{
            TokamakIcon,
            CloseIcon,
            dummyData: {
                Website : "https://tokamak.network",
                Description : "tokamak network’s operator1",
                OperatorAddress : "0xEA8e2eC08dCf4971bdcdfFFe21439995378B44F3",
                OperatorContract : "0x39A13a796A3Cd9f480C28259230D2EF0a7026033",
                ChainID : "9898",
                CommitCount : "66",
                RunningCommit : "4시간 전",
                RunningTime : "3달",
                CommissionRate : "2.5%",
                Reward : "0.00TON",
                TotalStaked : "4545479.24 TON",
                MyStaked : "5.22 TON",
                NotWithdrawable : "0.00 TON",
                Withdrawable : "0.00 TON",
                NewCommissionRate : "2.5%",
                NewCommissionRateChangedAt : "0",
                WithdrawalDelay : "93046 blocks"
                }
        }
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        }
    },
    components: {
        "button-main": ButtonMain,
        "operator-info-sub": OperatorInfoSub
    },
    computed: {
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
        }
    }
}
</script>

<style>
.modal-container {
    background-color: #FFFFFF;
    justify-content: center;
    top: 4.5%;
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
