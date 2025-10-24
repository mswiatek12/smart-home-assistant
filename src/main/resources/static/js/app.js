const chatBox = document.getElementById('chat-box');
const promptInput = document.getElementById('prompt');
const sendButton = document.getElementById('send-request');

function addMessage(text, sender) {
    const msg = document.createElement('div');
    msg.className = 'message ' + sender;
    msg.innerText = text;
    chatBox.appendChild(msg);
    chatBox.scrollTop = chatBox.scrollHeight;
}

async function sendMessage() {
    const prompt = promptInput.value.trim();
    if (!prompt) return;
    addMessage(prompt, 'user');
    promptInput.value = '';

    const response = await fetch('/api/chat', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ sessionId: '1', prompt })
    });
    const data = await response.text();
    addMessage(data, 'bot');
}

sendButton.addEventListener('click', sendMessage);
promptInput.addEventListener('keypress', e => {
    if (e.key === 'Enter') sendMessage();
});
