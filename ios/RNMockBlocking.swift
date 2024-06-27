//
//  RNMockBlocking.swift
//  ReactNativeWithKMP
//
//  Created by MacPro2 on 2024/6/27.
//

import Foundation

@objc(RNMockBlocking)
class RNMockBlocking: RCTEventEmitter {
  
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc(mockBlocking:)
  func mockBlocking(_ duration: NSNumber) {
    DispatchQueue.main.async {
      sleep(UInt32(duration.int32Value) / 1000)
    }
  }
}
