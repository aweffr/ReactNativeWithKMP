//
//  RNMockBlocking.m
//  ReactNativeWithKMP
//
//  Created by MacPro2 on 2024/6/27.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(RNMockBlocking, NSObject)

RCT_EXTERN_METHOD(mockBlocking:
                  (nonnull NSNumber *) duration)

@end
