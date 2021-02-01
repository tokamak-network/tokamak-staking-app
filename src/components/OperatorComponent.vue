<template>
  <view class="operator-wrap" >
  <touchable-opacity :on-press="pressed === false ? openOperator : null">
  <view class="operator-container">
    <image class="operator-img" :source="name === 'tokamak1' ? tokamak : name === 'DXM Corp' ? dxm : dsrv">
    </image>
    <view class="operator-text-container">
    <text class="operator-title">{{name}}</text>
    <touchable-opacity :on-press="pressed === true ? openInformation : openOperator">
    <text :class="{'selected': pressed === true, 'operator-content': pressed !== true}">{{pressed === false ? content : 'More Information'}}>
    </text>
      </touchable-opacity>
    </view>
    <touchable-opacity v-if="pressed === true" :on-press="closeOperator">
      <image class="operator-img-close" :source="pressed === true ? CloseIcon : null">
      </image>
    </touchable-opacity>
  </view>
  </touchable-opacity>
   <view 
        v-if="pressed"
        class="operator-detail">
     <view class="divider" />
       <view class="operator-detail-container">
       <view class="operator-detail-text">
         <text class="operator-detail-title">Commission Rate</text>
         <text class="operator-detail-content">2.5%</text>
       </view>
       <view class="operator-detail-text">
         <text class="operator-detail-title">Most recent Commit</text>
         <text class="operator-detail-content">a day ago</text>
       </view>
       <view class="operator-detail-text">
         <text class="operator-detail-title">My Staked</text>
         <text class="operator-detail-content">5,664.34TON</text>
       </view>
      <view class="divider" />
      <view class="operator-detail-text">
         <text class="operator-detail-title">Available Amount</text>
         <text class="operator-detail-content">4,598.24TON</text>
       </view>
       <text-input
        class="operator-detail-input"
      />
      <button-main title="Stake"/>
       </view> 
    </view>
  </view>
</template>
<script>
import ButtonMain from "@/components/ButtonMain"
import tokamak from "../../assets/tokamak.png"
import dxm from "../../assets/dxm.png"
import dsrv from "../../assets/dsrv.png"
import CloseIcon from "../../assets/icon-close.png"

export default {
  data() {
    return {
      tokamak,
      dxm,
      dsrv,
      CloseIcon,
      pressed: false,
      opendNow: null
    }
  },
  components: {
'button-main': ButtonMain
  },
     props: {
       name: {
         type: String
       },
       content: {
         type: String
       },
       Image: {
         type: String
       },
       navigation: {
      type: Object
    },
     },
     methods: {
       openOperator () {
         console.log("openoperator")
         this.pressed = true
        //  this.navigation.push('SelectedOperator', {name: "tokamak"} );
       },
       closeOperator () {
         console.log("closeoperator")
         this.pressed = false
       },
       openInformation () {
         console.log("openInfo")
       }
     }
}
</script>
<style scoped>
.operator-wrap {
  display: flex;
   flex-direction: column;
   width: 320px;
   padding: 15px 61px 15px 15px;
   border-width: 1;
   border-color: #e7ebf2;
   border-radius: 1px;
   background-color: #FFFFFF;
   margin-bottom: 15px;
}
.operator-container {
   display: flex;
   flex-direction: row;
}
.operator-img {
  width: 35px;
  height: 35px;
  margin-right: 18px;
}
.operator-img-close {
  width: 24px;
  height: 24px;
  align-self: flex-start;
  margin-left: 118px;
}
.operator-title {
  font-size: 17px;
  font-weight: 900;
  color: #131315
}
.operator-content {
  font-size: 11px;
  color: #86929d
}
.selected {
  font-size: 11px;
  color: #2a72e5;
  margin-bottom: 16px;
}
.divider {
  width: 280px;
  height: 1px;
  background-color: #e7ebf2;
  margin-bottom: 15px;
}
.operator-detail {
  display: flex;
  align-self: center;
}
.operator-detail-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 35px;
}
.operator-detail-input {
  width: 280px;
  height: 36px;
  border-width: 1px;
  border-radius: 1px;
  border-color: #dfe4ee;
  margin-bottom: 15px;
}
.operator-detail-btn {
  width: 280px;
  height: 40px;
  border-radius: 4px;
}
</style>