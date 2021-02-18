<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background">
        <view class="modal-container" :style="{height: windowHeight*0.91, width: windowWidth, top: windowHeight * 0.09}">
            <scroll-view :showsVerticalScrollIndicator="false">
                    <view class="modal-top">
                        <text class="modal-top-title">Tx Hash</text>
                        <touchable-opacity :style="{marginLeft: 'auto'}" :on-press="()=>close()">
                        <image :source=CloseIcon :style="{width: windowWidth*0.067, height: windowHeight*0.038, resizeMode: 'contain'}"></image>
                        </touchable-opacity>
                    </view>
                    <view class="divider" />
                    <view class="web-view-container" :style="{width: windowWidth, height: windowHeight*0.80}">
                        <web-view :url="uri"></web-view>
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
import WebViewComponent from "@/components/WebViewComponent"
import CloseIcon from "../../assets/icon-close.png";

export default {
    data() {
        return{
            CloseIcon,
        }
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        },
        uri: {
            type: String,
            default: ''
        }
    },
    components: {
        "web-view": WebViewComponent
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
    padding-right: 5.6%;
}
.modal-top-title {
    margin-left: 5%;
    font-size: 16px;
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
    top: -2.6%;
}
</style>
