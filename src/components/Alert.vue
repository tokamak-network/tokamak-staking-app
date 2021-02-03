<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background" :style="{height: windowHeight, width: windowWidth}">
        <view class="modal-container" :style="{height: windowHeight*height, width: windowWidth*width}">
            <text class="modal-container-text">Please check your TON amount</text>
            <touchable-opacity :style="{width: '100%'}" :on-press="()=>setModalVisible()">
            <button-main title="OK"></button-main>
            </touchable-opacity>
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

export default {
    data() {
        return{
            
        }
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        },
        width: {
            type: Number,
            default: 0
        },
        height: {
            type: Number,
            default: 0
        }
    },
    components: {
        "button-main": ButtonMain
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
        setModalVisible() {
            this.modalVisible = !this.modalVisible
        }
    }
}
</script>

<style>
.modal-container {
    align-self: center;
    background-color: #FFFFFF;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    top: 30%;
}
.modal-container-text {
    margin-bottom: 25.8%;
}
</style>