<template>
  <view class="container" >
    <animated:view
      class="growth-animated-view"
      :style="{
        height: 100,
        width: width,
        backgroundColor: interpolateColor,
      }"
    />
  </view>
</template>

<script>
import { Animated, Easing } from "react-native";

export default {
  data: function() {
    return {
      interpolateColor: 'black',
      width: 100,
    };
  },
  created: function() {
    this.growthValue = new Animated.Value(0);
    this.animatedColorValue = new Animated.Value(0);
  },
  mounted: function() {
    this.animateGrowth();
  },
  methods: {
    animateGrowth: function() {
      this.growthValue.setValue(0);
      this.animatedColorValue.setValue(0)
      Animated
        .timing(this.growthValue, {
          toValue: 1,
          duration: 2000,
          easing: Easing.linear
        })
        .start(() => {
            this.interpolateColor = this.animatedColorValue.interpolate({
            inputRange: [0, 150],
            outputRange: ["rgb(128,0,128)", "rgb(51, 250, 170)"]})
        })
        
        }
    
    }
  }
</script>

<style>
.growth-animated-view {
  background-color: "rgb(0, 138, 231)";
  align-self: center;
}

.container {
  justify-content: center;
  flex: 1;
}
</style>