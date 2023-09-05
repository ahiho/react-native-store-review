import RNStoreReview from './NativeRNStoreReview';
import { NativeEventEmitter, NativeModules } from 'react-native';
/**
 * Asks the user to rate the app in the App/Play Store.
 * @throws Will throw an Error if native module is not present or not supported by the OS version.
 */

let currentListener: NativeEventEmitter | undefined = undefined;
let eventEmiter = undefined;
export const requestReview = () => {
  if (!RNStoreReview) {
    throw new Error(
      'StoreReview native module not available, did you forget to link the library?'
    );
  }

  return RNStoreReview.requestReview();
};

export type RequestReviewResult = {
  success: boolean;
  error?: string;
  reviewInfo?: string;
};

export const addRequestReviewListener = (
  listener: (RequestReviewResult) => void
) => {
  if (!RNStoreReview) {
    throw new Error(
      'StoreReview native module not available, did you forget to link the library?'
    );
  }

  if (!eventEmiter) {
    eventEmiter = new NativeEventEmitter(NativeModules.RNStoreReview);
  }
  
  if (!!currentListener) {
    currentListener.remove();
    currentListener = undefined;
  }

  currentListener = eventEmiter.addListener('EmitStoreReviewResult', listener);
};
