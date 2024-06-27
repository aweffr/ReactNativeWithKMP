/**
 * @format
 */
import React, { Fragment } from "react";
import { AppRegistry } from "react-native";
import App from "./src/App";
import Toast from "react-native-toast-message";

function AppWithToast() {
  return (
    <Fragment>
      <App />
      <Toast />
    </Fragment>
  );
}

AppRegistry.registerComponent("ReactNativeWithKMP", () => AppWithToast);
