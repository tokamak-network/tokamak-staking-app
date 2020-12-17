import { cloneDeep } from 'lodash';
import Vue from 'vue-native-core';
import Vuex from 'vuex';
Vue.use(Vuex);

const initialState = {
    loading: false,
    signIn: false,
    user: ''
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
    }, 
    actions: {
        logout (context) {
            context.commit('SET_INITIAL_STATE');
          },
          async signIn (context, user) {
            context.commit('SIGN_IN', true);
            context.commit('SET_USER', user);
          }
    }, 
    getters:{

    }
});