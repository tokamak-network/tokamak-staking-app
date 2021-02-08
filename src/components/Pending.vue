<template>
        <view v-if="modalVisible===true" class="modal-container" :style="{width: windowWidth*0.358, height: windowHeight*0.059, marginTop: windowHeight * 0.094}">
            <activity-indicator class="modal-indicator" size="small" color="#FFFFFF" />
            <text class="modal-text">Tx Pending</text>
        </view>
</template>

<script>
import {
  Dimensions
} from "react-native";
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
    },
    methods: {
        
    },
    computed: {
         windowWidth() {
            return Dimensions.get("window").width;
        },
            windowHeight() {
            return Dimensions.get("window").height;
        },
    }
}
</script>
<style>
.modal-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  background-color: #2a72e5;
  border-radius: 20px;
  align-self: center;
  opacity: 0.895;
}
.modal-indicator {
    margin-right: 7.6%;
}
.modal-text {
    font-size: 14px;
    color: #FFFFFF;
}
</style>
