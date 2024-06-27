//
//  RNShared.m
//  ReactNativeWithKMP
//
//  Created by MacPro2 on 2024/6/27.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(RNShared, NSObject)

RCT_EXTERN_METHOD(getLocalTodos:
                  (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHttpTodos:
                  (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHttpTodosAndSendEvent:
                  (NSString *) taskId)

@end
