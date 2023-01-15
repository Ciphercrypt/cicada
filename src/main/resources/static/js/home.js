let chatDiv = document.getElementById('chat-page')
let sendBtn = document.getElementById('send-btn')
let messageList = document.getElementById('message-list')
let contactList = document.getElementById('contact-list')
let messageInput = document.getElementById('message-input')
let currentUser = "hreshi"
let myself = "hreshi"

let userList = ["Hreshi", "mspatild7", "aakanksha2812", "Hrushikeshkale"]
let messageStore = []
messageStore["Hreshi"] = []
messageStore["mspatild7"] = []
messageStore["aakanksha2812"] = []
messageStore["Hrushikeshkale"] = []


async function req(url) {
    let res = await fetch(url);
    console.log(await res.text());
}

sendBtn.addEventListener('click', function (event) {
    event.preventDefault()
    let message = {
        sender:myself,
        recver:currentUser,
        content:messageInput.value
    }
    messageStore[currentUser].push(message)
    appendMessage(message)
    sendMessage(message.content)
    messageInput.value = ""
})

function makeMessage(message) {
    let messageDiv = document.createElement('div')
    let nameP = document.createElement('span')
    nameP.innerText = message.sender 
    nameP.className = " username"
    let messageP = document.createElement('span')
    messageP.innerText = message.content
    messageP.className = " ms-1"
    messageDiv.append(nameP)
    messageDiv.append(messageP)
    if(message.sender === myself) {
        messageDiv.className = "card text-wrap mt-3 bg-green text-secondary "    
    } else {
        messageDiv.className = "card text-wrap left-message bg-blue mt-3 text-secondary "
    }
    return messageDiv
}

function makeContact(name) {
    let contactDiv = document.createElement('div')
    let nameP = document.createElement('span')
    nameP.innerText = name
    contactDiv.setAttribute("id", name)
    nameP.className = " ms-1 btn"
    contactDiv.append(nameP)
    contactDiv.className = " card text-wrap left-message mt-3 text-secondary "
    return contactDiv
}
function appendMessage(message) {
    messageList.append(makeMessage(message))
    messageList.scrollTop = messageList.scrollHeight
}

function handleContactClickEvent(element) {
    messageInput.value = ""
    while(messageList.firstChild) {
        messageList.removeChild(messageList.firstChild)
    }
    let sender = element.getAttribute("id")
    messageStore[sender].forEach(message => {
        appendMessage(message)
    })
}

function addUsersToContactList() {
    userList.forEach(name => {
        let ele = makeContact(name);
        ele.addEventListener('click', function (event) {
            currentUser = ele.getAttribute("id")
            handleContactClickEvent(ele)
        })
        contactList.append(ele)
    })
}

function handleIncomingMessage(message) {
    messageStore[message.sender].push(message);
    if(message.sender === currentUser) {
        appendMessage(message)
        console.log("apple"+ message)
    }
}

let stompClient = null;

function connect() {
    let socket = new SockJS("/ws/connect");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe("/messages/"+myself, function (data) {
            console.log("Message Received : ");
            let msg = JSON.parse(data.body)
            let message = {
                sender:msg.sender,
                recver:msg.recver,
                content:msg.content
            }
            handleIncomingMessage(message)
        })

    });
}
function sendMessage(message) {
    let data = {
        sender:myself,
        recver:currentUser,
        content:message
    };

    stompClient.send("/ms/send", {}, JSON.stringify(data));
}

async function setMyself() {
    let res = await fetch("/user/data");
    let name = await JSON.parse(await res.text());

    myself = name.username
    for(let i = 0;i < userList.length;i++) {
        if(userList[i] === myself) {
            userList.splice(i,1)
        }
    }
    currentUser = userList[0]
    addUsersToContactList()
    connect()
}

function logout() {
    fetch("/logout", {method:"POST"})
}

setMyself()
