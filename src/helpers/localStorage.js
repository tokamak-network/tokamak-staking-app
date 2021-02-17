import AsyncStorage from '@react-native-async-storage/async-storage';
import { log } from 'react-native-reanimated';

export async function getPendingTransactions (user, networkId ) {
  try{const pendingTransactions = await AsyncStorage.getItem(`pending-transacs-${user}-${networkId}`);
  if (pendingTransactions !== null) {
      return pendingTransactions;
    } else {
      return [];
    }}
    catch(error){

    }
}

export async function setPendingTransactions (pendingTransactions=[], user, networkId) {
  await AsyncStorage.setItem(`pending-transacs-${user}-${networkId}`, JSON.stringify(pendingTransactions) )
}