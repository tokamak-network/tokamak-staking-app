<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
        <view class="modal-container" :style="{height: windowHeight*0.428, width: windowWidth}">
            <scroll-view>
                <view class="modal-content">
                    <view class="modal-top">
                        <text class="modtal-top-title">Select an operator</text>
                        <touchable-opacity :on-press="()=>close()">
                        <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain', marginLeft: 'auto'}"></image>
                        </touchable-opacity>
                    </view>
                    <view class="divider" />
                    <view class="modal-bottom">
                        <view class="modal-bottom-row">
                            <image :style="{width: windowWidth*0.083, height: windowHeight*0.047, resizeMode: 'contain'}" :source=TokamakIcon></image>
                            <text class="modal-bottom-row-text">Tokamak1</text>
                        </view>
                        <view class="modal-bottom-row">
                            <image :style="{width: windowWidth*0.083, height: windowHeight*0.047, resizeMode: 'contain'}" :source=DXMIcon></image>
                            <text class="modal-bottom-row-text">DXM Corp</text>
                        </view>
                        <view class="modal-bottom-row">
                            <image :style="{width: windowWidth*0.083, height: windowHeight*0.047, resizeMode: 'contain'}" :source=DSRVIcon></image>
                            <text class="modal-bottom-row-text">DSRV</text>
                        </view>
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
import TokamakIcon from "../../assets/TokamakLogo.png";
import CloseIcon from "../../assets/icon-close.png";
import DSRVIcon from "../../assets/dsrv.png";
import DXMIcon from "../../assets/dxm.png";
import { mapState } from 'vuex';

export default {
    data() {
        return{
            TokamakIcon,
            CloseIcon,
            DSRVIcon,
            DXMIcon
        }
    },
    computed: {
    ...mapState([
      'operators',
    ]),
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        }
    },
    components: {
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
        },
    }
}
</script>

<style>
.modal-container {
    background-color: #FFFFFF;
    justify-content: center;
    top: 57.2%;
    padding-top: 9.9%;
    padding-left: 5.6%;
    padding-right: 5.6%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
}
.modal-content {
    display: flex;
    flex-direction: column;
}
.modal-top {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 7.7%;
}
.modtal-top-title {
    font-size: 16px;
    color: #131315;
}
.divider {
  width: 100%;
  height: 1px;
  background-color: #dfe4ee;
  margin-bottom: 5.8%;
}
.modal-bottom {
    display: flex;
    flex-direction: column;
}
.modal-bottom-row {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 10.2%;
}
.modal-bottom-row-text {
    margin-left: 4.7%;
    font-size: 16px;
    font-weight: bold;
    color: #131315;
}
</style>