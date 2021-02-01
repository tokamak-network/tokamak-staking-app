import { cloneDeep } from 'lodash';
import Vue from 'vue-native-core';
import Vuex from 'vuex';
import { NativeModules } from 'react-native';
const { BlockchainModule } = NativeModules;

Vue.use(Vuex);

const initialState = {
    loaded: false,
    signIn: false,
    user: '0x660DE9AE5Dd7C8dE4C5c9A8dAB64AF706a9F8a57', 
    TONbalance: {
      value: 1, 
      symbol: " TON"
    }, 
    ETHbalance: {
      value: 1, 
      symbol: " ETH"
    }, 
    powerTONbalance: {
      value: 100, 
      symbol: " Power"
    },
    stakedAmount: {
      value: 6.77, 
      symbol: " TON"
    },
    rewards: {
      value: 30, 
      symbol: " TON"
    }
}
const getInitialState = () => initialState;

export default new Vuex.Store({
  state: cloneDeep(initialState),
    mutations: {
        SET_INITIAL_STATE: (state) => {
            const initialState = getInitialState();
            Object.keys(initialState).forEach(key => {
              state[key] = initialState[key];
            });
          },
          SIGN_IN: (state) => {
            state.signIn = true;
          },
          SET_USER: (state, user) => {
            state.user = user;
          },
          SET_LOADING: (state, loaded) => {
            state.loaded = loaded;
          }, 
          SET_ETHBALACE: (state, balance) => {
            state.ETHbalance.value = balance;
          }
    }, 
    actions: {
        logout (context) {
            context.commit('SET_INITIAL_STATE');
          },
          async signIn (context) {
            // BlockchainModule.initialize();
            setTimeout(() => {
              context.commit ('SET_LOADING', true)
            },15)
           
            
          },
          login (context) {
            context.commit('SIGN_IN', true);
            //  BlockchainModule.setupAccount(
            //   (address, balance) => {
            //     context.commit('SET_USER', address);
            //     context.commit('SET_ETHBALACE', balance);
            //     console.log(balance);
                
            //   }
            // );
          }
    }, 
    getters:{

    }
});