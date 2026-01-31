<template>
  <div class="app">
    <h2 class="title">📝 Todo List</h2>

    <div class="add-todo">
      <input
        v-model="newTitle"
        placeholder="Enter a new todo..."
      />
      <button class="btn primary" @click="addTodo">
        Add
      </button>
    </div>

    <div class="todo-list">
      <div class="todo-item" v-for="todoItem in todoList" :key="todoItem.id">
        <span class="todo-title">{{ todoItem.title }}</span>
        <div class="actions">
          <button class="btn green" @click="updateClicked(todoItem.id)">Edit</button>
          <button class="btn danger" @click="deleteClicked(todoItem.id)">Delete</button>
        </div>
      </div>
    </div>

    <div v-if="updateMode" class="overlay">
      <div class="modal">
        <h3>Edit Todo</h3>
        <input v-model="currTodo.title" />
        <div class="modal-actions">
          <button class="btn primary" @click="updateSaveclicked">Save</button>
          <button class="btn" @click="updateMode = false">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

defineProps<{}>()

const count = ref(0)

const todoList = ref([])

const newTitle = ref('')

const updateMode = ref(false)

const currTodo = ref({id: -1, title: ''})
const updateSaveclicked = () => {
  const title = currTodo.value.title.trim();
  if (title === '') {
    return;
  }

  fetch('http://localhost:8080/todo', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ title: title, id: currTodo.value.id }),
  })
  .then(response => response.json())
  .then(updatedTodo => {
    const index = todoList.value.findIndex(item => item.id === updatedTodo.id);
    if (index !== -1) {
      todoList.value[index] = updatedTodo;
    }
    updateMode.value = false;
    currTodo.value = '';
  })
  .catch(error => {
    console.error('Error updating todo:', error);
  });
}
const updateClicked = (id: number) => {
  updateMode.value = true;
  const todo = todoList.value.find(item => item.id === id);
  currTodo.value = { ...todo };
}

const deleteClicked = (id: number) => {
  try{
    fetch(`http://localhost:8080/todo/${id}`, {
      method: 'DELETE',
    })
    .then(response => {
      if (response.ok) {
        todoList.value = todoList.value.filter(item => item.id !== id);
      } else {
        console.error(`Failed to delete item with id ${id}.`);
      }
    })
    .catch(error => {
      console.error('Error during deletion:', error);
    });
  } catch (error) {
    console.error('Unexpected error:', error);
  }
}

const addTodo = () => {
  const title = newTitle.value.trim();
  if (title === '') {
    return;
  }

  const newTodo = { title: title };

  fetch('http://localhost:8080/todo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(newTodo),
  })
  .then(response => response.json())
  .then(addedTodo => {
    todoList.value.push(addedTodo);
    newTitle.value = '';
  })
  .catch(error => {
    console.error('Error adding todo:', error);
  });
}

onMounted(() => {
  fetch('http://localhost:8080/todo')
    .then(response => response.json())
    .then(data => {
      todoList.value = data;
      console.log('Fetched todo list:', todoList.value);
    })
    .catch(error => {
      console.error('Error fetching todo list:', error);
    });
})
</script>

<style scoped>

.app {
  max-width: 500px;
  margin: 40px auto;
  font-family: Arial, sans-serif;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.add-todo {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

input {
  flex: 1;
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-radius: 8px;
  background-color: black;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

.todo-title {
  font-weight: 500;
  flex: 1;     
  min-width: 0;       
  word-break: break-word;
}

.actions {
  display: flex;
  gap: 6px;
}

.btn {
  padding: 6px 10px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-size: 13px;
  background-color: #e0e0e0;
}

.btn:hover {
  opacity: 0.85;
}

.primary {
  background-color: #4f46e5;
  color: white;
}

.danger {
  background-color: #dc2626;
  color: white;
}

.green{
  background-color: #22c55e;
  color: white;
}

/* Modal */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
}

.modal h3 {
  margin-bottom: 10px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}

</style>