<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background" :style="{height: windowHeight, width: windowWidth}">
        <view class="modal-container" :style="{height: windowHeight*height, width: windowWidth*width}">
            <text class="modal-container-text">{{message}}</text>
            <touchable-opacity :style="{width: windowWidth*0.361}" :on-press="()=>setModalVisible()">
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
        },
         message: {
             type:String, 
             default:''
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
            this.modalVisible = false
              this.$emit('closePopup', false)
        }
    }
}
</script>

<style>
.modal-background {
   background-color:rgba( 0, 0, 0, 0.45 );
}
.modal-container {
    align-self: center;
    background-color: #FFFFFF;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 11%;
    top: 38%;
    border-radius: 10px;
}
.modal-container-text {
    font-size: 15px;
    color: #3e495c;
    margin-bottom: 15%;
}
</style>