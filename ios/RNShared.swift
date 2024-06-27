//
//  RNShared.swift
//  ReactNativeWithKMP
//
//  Created by MacPro2 on 2024/6/27.
//

import Foundation
import shared

@objc(RNShared)
class RNShared: RCTEventEmitter {

  override static func requiresMainQueueSetup() -> Bool {
    return true
  }

  var todoService = TodoService()

  private func todoAsReactMap(todo: Todo) -> [String: Any] {
    return ["id": todo.id, "title": todo.title, "completed": todo.completed]
  }


  @objc(getLocalTodos:rejecter:)
  func getLocalTodos(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    let todos = todoService.getLocalTodos()
    let jsPayload = todos.map { todo in
      todoAsReactMap(todo: todo)
    }
    resolve(jsPayload)
  }

  @objc(getHttpTodos:rejecter:)
  func getHttpTodos(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    DispatchQueue.main.async {
      print("getTodosHttp called! current thread: \(Thread.current)")
      self.todoService.getHttpTodos { todos, error in
        print("getTodosHttp finished! current thread: \(Thread.current)")
        if let err = error {
          reject("RNSharedError", "getHttpTodos fail", err)
        } else if let _todos = todos {
          let result = _todos.map { todo in
            ["id": todo.id, "title": todo.title, "completed": todo.completed]
          }
          print("Got todos count: \(result.count)")
          resolve(result)
        } else {
          reject("RNSharedError", "getHttpTodos fail. todos is nil", nil)
        }
      }
    }
  }
  
  @objc(getHttpTodosAndSendEvent:)
  func getHttpTodosAndSendEvent(_ taskId: String) {
    todoService.getHttpTodosAndSendEvent(taskId: taskId)
  }

  override init() {
    super.init()
    let greetStr = Greeting().greet()
    print("=====\nRNShared init called! greetStr:\(greetStr)\n=====\n")

    todoService.registerSendEventCallback { payload in
      print("callback fired from shared module! \(Thread.current)")
      let jsPayload = [
        "taskId": payload.taskId,
        "message": payload.message,
        "todos": payload.todos.map { todo in
          self.todoAsReactMap(todo: todo)
        }
      ]
      self.sendEvent(withName: TodoService.companion.EVENT_NAME_TODOS_RECEIVED, body: jsPayload)
    }
  }
  
  override func supportedEvents() -> [String]! {
    return [TodoService.companion.EVENT_NAME_TODOS_RECEIVED]
  }

  override func constantsToExport() -> [AnyHashable : Any]! {
    return ["EVENT_NAME_TODOS_RECEIVED": TodoService.companion.EVENT_NAME_TODOS_RECEIVED]
  }
}
