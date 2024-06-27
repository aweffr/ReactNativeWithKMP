import React, { useCallback, useEffect, useState } from "react";
import { SafeAreaView, View, Text, FlatList, Button } from "react-native";
import { RNShared, Todo } from "./utils/RNShared.ts";
import TodoItem, { todoKeyExtractor } from "./components/TodoItem.tsx";
import Toast from "react-native-toast-message";

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
        setTodoList((prev) => [...payload.todos, ...prev]);
      },
    );
    return () => sub.remove();
  }, []);

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: "white" }}>
      <View>
        <Text>RNShared Available: {String(RNShared.isAvailable())}</Text>
      </View>
      <View style={{ flexDirection: "row" }}>
        <Button title="清空" onPress={clearTodoList} />
        <Button title="local" onPress={onPressLocal} />
        <Button title="http" onPress={onPressHttp} />
        <Button title="event" onPress={onPressEvent} />
      </View>
      <FlatList
        style={{ flex: 1 }}
        keyExtractor={todoKeyExtractor}
        data={todoList}
        renderItem={({ item }: { item: Todo }) => {
          return <TodoItem todo={item} />;
        }}
      />
    </SafeAreaView>
  );
}

export default App;
