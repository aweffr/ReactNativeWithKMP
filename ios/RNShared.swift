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
  
  override init() {
    super.init()
    let greetStr = Greeting().greet()
    print("=====\nRNShared init called! greetStr:\(greetStr)\n=====\n")
  }
}
