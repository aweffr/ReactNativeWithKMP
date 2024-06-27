import { NativeModules } from "react-native";
import Toast from "react-native-toast-message";

export function mockBlockingOnJS(duration: number = 1000) {
  console.log("mockBlockingOnJS start!");
  const until = Date.now() + duration;
  while (Date.now() < until) {
    Math.random();
  }
  console.log("mockBlockingOnJS done!");
  setTimeout(() => {
    Toast.show({ type: "success", text1: "mockBlockingOnJS done!" });
  });
}

export function mockBlockingOnNative(duration: number = 1000) {
  const RNMockBlocking = NativeModules.RNMockBlocking;
  if (RNMockBlocking) {
    RNMockBlocking.mockBlocking(duration);
    setTimeout(() => {
      Toast.show({ type: "success", text1: "mockBlockingOnNative done!" });
    });
  } else {
    console.warn("Native module RNMockBlocking not found!");
  }
}
