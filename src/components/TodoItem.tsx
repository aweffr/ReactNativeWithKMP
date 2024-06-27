import React from "react";
import { View, Text } from "react-native";
import { Todo } from "../utils/RNShared.ts";

function TodoItem({ todo }: { todo: Todo }) {
  return (
    <View style={{ paddingVertical: 5, borderTopColor: "#DDD", borderTopWidth: 1 }}>
      <Text>id: {todo.id}</Text>
      <Text>title: {todo.title}</Text>
      <Text>completed: {todo.completed ? "✅" : "⭕"}</Text>
    </View>
  );
}

export function todoKeyExtractor(todo: Todo) {
  return `id-${todo.id}`;
}

export default TodoItem;
