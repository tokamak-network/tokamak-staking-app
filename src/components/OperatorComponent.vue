<template>
  <view class="operator-wrap">
    <touchable-opacity :on-press="pressed === false ? openOperator : null">
      <view class="operator-container">
        <image
          class="operator-img"
          :source="
            name === 'Tokamak1' ? tokamak : name === 'DXM Corp' ? dxm : dsrv
          "
        >
        </image>
        <view class="operator-text-container">
          <text class="operator-title">{{ name }}</text>
          <touchable-opacity
            :on-press="pressed === true ? openInformation : openOperator"
          >
            <text
              :class="{
                selected: pressed === true,
                'operator-content': pressed !== true,
              }"
              >{{ pressed === false ? content : "More Information" }}
            </text>
          </touchable-opacity>
        </view>
        <touchable-opacity v-if="pressed === true" :on-press="closeOperator">
          <image
            class="operator-img-close"
            :source="pressed === true ? CloseIcon : null"
          >
          </image>
        </touchable-opacity>
      </view>
    </touchable-opacity>
    <view v-if="pressed" class="operator-detail">
      <view>
        <view class="divider" :style="{ marginBottom: '1.4%' }" />
      </view>
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
        <view
          class="divider"
          :style="{ marginTop: '1.4%', marginBottom: '2.9%' }"
        />
        <view class="operator-detail-text">
          <text class="operator-detail-title">Available Amount</text>
          <text class="operator-detail-content">4,598.24TON</text>
        </view>
        <text-input class="operator-detail-input" pla />
        <button-main title="Stake" />
      </view>
    </view>
  </view>
</template>
<script>
import ButtonMain from "@/components/ButtonMain";
import tokamak from "../../assets/tokamak.png";
import dxm from "../../assets/dxm.png";
import dsrv from "../../assets/dsrv.png";
import CloseIcon from "../../assets/icon-close.png";

export default {
  data() {
    return {
      tokamak,
      dxm,
      dsrv,
      CloseIcon,
      pressed: false,
      opendNow: null,
    };
  },
  components: {
    "button-main": ButtonMain,
  },
  props: {
    name: {
      type: String,
    },
    content: {
      type: String,
    },
    Image: {
      type: String,
    },
    navigation: {
      type: Object,
    },
  },
  methods: {
    openOperator() {
      console.log("openoperator");
      this.pressed = true;
      //  this.navigation.push('SelectedOperator', {name: "tokamak"} );
    },
    closeOperator() {
      console.log("closeoperator");
      this.pressed = false;
    },
    openInformation() {
      console.log("openInfo");
    },
  },
};
</script>
<style scoped>
.operator-wrap {
  display: flex;
  flex-direction: column;
  width: 88.9%;
  border-width: 1px;
  border-color: #e7ebf2;
  border-radius: 1px;
  background-color: #ffffff;
  margin-bottom: 2.3%;
  align-self: center;
}
.operator-container {
  display: flex;
  flex-direction: row;
  padding-top: 6.9%;
  padding-left: 6.9%;
  padding-bottom: 6.1%;
}
.operator-img {
  width: 35px;
  height: 35px;
  margin-right: 5.6%;
}
.operator-img-close {
  width: 24px;
  height: 24px;
  align-self: flex-end;
  margin-left: 36.9%;
}
.operator-title {
  font-size: 17px;
  font-weight: bold;
  color: #131315;
}
.operator-content {
  font-size: 11px;
  color: #86929d;
}
.selected {
  font-size: 11px;
  color: #2a72e5;
  margin-bottom: 4.6%;
}
.divider {
  width: 100%;
  height: 1px;
  background-color: #e7ebf2;
}
.operator-detail {
  display: flex;
  align-self: center;
  padding-bottom: 6.1%;
}
.operator-detail-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-top: 2.9%;
  padding-bottom: 2.6%;
}
.operator-detail-input {
  width: 100%;
  height: 14%;
  border-width: 1px;
  border-radius: 4px;
  border-color: #dfe4ee;
  margin-top: 2.9%;
  margin-bottom: 4.3%;
}
.operator-detail-btn {
  width: 77.8%;
  height: 6.2%;
  border-radius: 4px;
}
</style>
