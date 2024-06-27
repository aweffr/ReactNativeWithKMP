import React, { useCallback, useEffect, useState } from "react";
import { SafeAreaView, View, Text, ScrollView, Button } from "react-native";
import { RNShared, Todo } from "./utils/RNShared.ts";
import TodoItem, { todoKeyExtractor } from "./components/TodoItem.tsx";
import Toast from "react-native-toast-message";
import Carousel from "./components/Carousel.tsx";
import { mockBlockingOnJS, mockBlockingOnNative } from "./utils/mock-blocking.ts";

function App() {
  const [todoList, setTodoList] = useState<Todo[]>([]);

  const clearTodoList = useCallback(() => {
    setTodoList([]);
  }, []);

  const onPressLocal = useCallback(() => {
    clearTodoList();
    RNShared.getLocalTodos().then((todos) => {
      setTodoList(todos);
    });
  }, [clearTodoList]);

  const onPressHttp = useCallback(() => {
    clearTodoList();
    RNShared.getHttpTodos().then((todos) => {
      console.log(`getHttpTodos finish! todos count: ${todos.length}`);
      setTodoList(todos);
    });
  }, [clearTodoList]);

  const onPressEvent = useCallback(() => {
    clearTodoList();
    const taskId = `fetchTodo-${Date.now()}`;
    RNShared.getHttpTodosAndSendEvent(taskId);
  }, [clearTodoList]);

  useEffect(() => {
    const sub = RNShared.getEventEmitter().addListener(
      RNShared.EVENT_NAME_TODOS_RECEIVED,
      (payload: { taskId: string; message: string; todos: Todo[] }) => {
        Toast.show({
          type: "success",
          text1: `got todos! taskId:${payload.taskId}`,
          text2: payload.message,
          visibilityTime: 1500,
        });
        setTodoList((prev) => {
          const next = [...payload.todos, ...prev];
          next.sort((a, b) => b.id - a.id);
          return next;
        });
      },
    );
    return () => sub.remove();
  }, []);

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: "white", paddingHorizontal: 5 }}>
      <View>
        <Text>RNShared Available: {String(RNShared.isAvailable())}</Text>
      </View>
      <View style={{ marginVertical: 10 }}>
        <Text>动画模式: Native</Text>
        <Carousel useNativeDriver={true} />
      </View>
      <View style={{ marginVertical: 10 }}>
        <Text>动画模式: JS</Text>
        <Carousel useNativeDriver={false} />
      </View>
      <View style={{ flexDirection: "row" }}>
        <Button title="阻塞JS" onPress={() => mockBlockingOnJS(3000)} />
        <Button title="阻塞Native" onPress={() => mockBlockingOnNative(3000)} />
      </View>
      <View style={{ flexDirection: "row" }}>
        <Button title="清空" onPress={clearTodoList} />
        <Button title="local" onPress={onPressLocal} />
        <Button title="http" onPress={onPressHttp} />
        <Button title="event" onPress={onPressEvent} />
      </View>
      <ScrollView style={{ flex: 1 }}>
        {todoList.map((todo) => (
          <TodoItem todo={todo} key={todoKeyExtractor(todo)} />
        ))}
      </ScrollView>
    </SafeAreaView>
  );
}

export default App;
