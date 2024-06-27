import React, { useEffect, useMemo, useRef } from "react";
import { Animated, Dimensions, Easing, View } from "react-native";

function Carousel({ useNativeDriver }: { useNativeDriver: boolean }) {
  const deviceWidth = useMemo(() => {
    return Dimensions.get("window").width;
  }, []);

  const ballAnimationRef = useRef(new Animated.Value(0));
  const ballAnimation = ballAnimationRef.current;
  const ballDiameter = 50;
  const ballRadius = ballDiameter / 2;
  const ballShift = deviceWidth - ballDiameter;

  useEffect(() => {
    const startAnimation = () => {
      Animated.sequence([
        Animated.timing(ballAnimation, {
          toValue: ballShift,
          duration: 2000,
          easing: Easing.ease,
          useNativeDriver: useNativeDriver,
        }),
        Animated.timing(ballAnimation, {
          toValue: 0,
          duration: 2000,
          easing: Easing.ease,
          useNativeDriver: useNativeDriver,
        }),
      ]).start(() => startAnimation()); // Loop the animation
    };

    startAnimation();
  }, [useNativeDriver, ballAnimation, ballShift]);

  return (
    <View style={{ height: 2.5 * ballRadius, width: deviceWidth }}>
      <Animated.View
        style={{
          width: ballDiameter,
          height: ballDiameter,
          borderRadius: ballRadius,
          backgroundColor: "orange",
          position: "absolute",
          transform: [{ translateX: ballAnimation }],
        }}
      />
    </View>
  );
}

export default Carousel;
