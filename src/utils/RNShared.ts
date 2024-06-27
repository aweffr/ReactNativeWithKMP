import { NativeEventEmitter, NativeModules } from "react-native";

export interface Todo {
  id: number;
  title: string;
  completed: boolean;
}

export class RNShared {
  static getModule() {
    return NativeModules.RNShared;
  }

  static isAvailable() {
    return Boolean(RNShared.getModule());
  }

  static get EVENT_NAME_TODOS_RECEIVED() {
    return RNShared.getModule().EVENT_NAME_TODOS_RECEIVED;
  }

  static _eventEmitter: NativeEventEmitter | null = null;

  static getEventEmitter(): NativeEventEmitter {
    if (!RNShared._eventEmitter) {
      RNShared._eventEmitter = new NativeEventEmitter(RNShared.getModule());
    }
    return RNShared._eventEmitter;
  }

  static async getLocalTodos(): Promise<Array<Todo>> {
    return RNShared.getModule().getLocalTodos();
  }

  static async getHttpTodos(): Promise<Array<Todo>> {
    return RNShared.getModule().getHttpTodos();
  }

  static getHttpTodosAndSendEvent(taskId: string) {
    RNShared.getModule().getHttpTodosAndSendEvent(taskId);
  }
}
