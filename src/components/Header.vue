<template>
    <view>
         <view class="logo-container" >
           <nb-button v-if="!back"
          transparent
          :onPress="openDrawer"
          class="hamburger-button">
          <nb-icon class="hamburger-icon" name="menu" style="fontSize: 48"/>
        </nb-button>
         <nb-button v-else
          transparent 
          :onPress="() => this.props.navigation.goBack()"
           class="hamburger-button">
          <nb-icon class="hamburger-icon" name="arrow-back" style="fontSize: 40"/>
        </nb-button>
        <image class="logo" :style="{width: 162, height: 39}" :source="require('../../assets/logo-small.png')" />
      </view>
    </view>
</template>
<script>
import React from 'react';
export default {
    props: {
     navigation: {
      type: Object
    },
    back: {
      type: Boolean
    }
  },
     methods: {
        openDrawer () {
          this.navigation.openDrawer();
        }
    }
}
</script>
<style scoped>
.logo-container {
  display: flex;
  padding: 15px 5px 0px 5px;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;

}
.logo {
  display: flex;
}

.hamburger-icon {
  color: #2a72e5;
  margin-left: -5px;
}
</style>