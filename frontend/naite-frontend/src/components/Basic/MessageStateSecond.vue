<template>
    <div id='messagestatesecond'>
        <div class='message2ndTitle'>
            <h4>채팅목록</h4>
            <i class="fas fa-times-circle" @click='closeSecond'></i>
        </div>
        <div class='messageList' v-for='(message,idx) in myChatList' :key="idx">
            <div class="messageListBox" @click='messageDetail(message.roomNo,message.otherNick,message.otherUserNo,message.otherPic)'>
                <img :src='message.otherPic' alt="">
                <div>
                    <span>{{message.otherNick}}</span><br>
                    <div class='messageListList'>

                        <div>
                            {{message.lastMessage ? message.lastMessage: `${message.otherNick}님과의 채팅방이 개설되었습니다!` }}
                        </div>
                        <div>
                            {{createdSimple(message.lastMessageTime)}}
                        </div>
                    </div>
                </div>
                
                <span class='newAlert' v-if='message.new&&detailNick!==message.otherNick'>새로운 메시지</span>
            </div>
            
            

        </div>
    </div>
</template>
<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL


export default {
    name:'MessageStateSecond',
    data:function(){
        return {
            myChatList:[],
        }
    },
    props:{
        finalMessage:[Array],
        detailNick:String,
    },
    methods:{
        renderChatList:function(){
            axios.get(`${SERVER_URL}/chat/room`,this.setToken())
                .then(res => {
                    this.myChatList = res.data                       
                })
                .catch(() => {                    
                    location.reload()
                })
        },
        messageDetail:function(roomNo,otherNick,otherUserNo,otherPic){
            this.$emit('messageDetail',roomNo,otherNick,otherUserNo,otherPic)
        },
        closeSecond:function(){
            this.$emit('closeSecond')
        },
        setToken:function(){
            const token = localStorage.getItem('jwt')
            const config = {
                headers: {
                'auth-token':`${token}`
                }
            }        
            return config 
        }

    },
    created(){     
        this.renderChatList()   
        let that = this
        window.addEventListener('keyup',function(e){
            if (e.key.toLowerCase()==='escape'){
                that. closeSecond()
            }
        })
    },
    watch:{
    },
    computed:{
        createdSimple(){
            return (date) => {
                if (!date){
                    return;
                }
                var dateArray = date.split('-')
                if (date.length > 10) {
                    var timeArray = dateArray[2].split(' ')
                    return dateArray[1]+'/'+dateArray[2][0] + dateArray[2][1]+' '+timeArray[0] +':'+timeArray[1][0]+timeArray[1][1]
                } else {
                    return date
                }
            }
        }
    }
}
</script>

<style>
#messagestatesecond {
    position:relative;
    width:100%;
    height:100%;
    overflow: auto;
    overflow-x: hidden;
    background-color: white;
    font-family: font1;
    padding:0;
    border: 1px solid #3F9F47;
    border-radius: 10px;
}
#messagestatesecond::-webkit-scrollbar { width: 10px;}
#messagestatesecond::-webkit-scrollbar-track { background-color:rgba(0, 0, 0, 0.5);border-radius: 10px;  }
#messagestatesecond::-webkit-scrollbar-thumb { background: #e6e3e0f5;border-radius: 10px;  }
#messagestatesecond::-webkit-scrollbar-thumb:hover { background: #e68c42; } 
#messagestatesecond::-webkit-scrollbar-thumb:active { background: #e68c42; }
#messagestatesecond::-webkit-scrollbar-button { display: none; } 


.message2ndTitle {    
    
    padding: 0 10px;
    display:flex;
    justify-content: space-between;
    background-color: #a87a4fda;
    border-bottom: 3px solid #ffffff;
    color:white;
}

.message2ndTitle > i {
    margin-top: 2%;
}
.message2ndTitle > .fa-times-circle{
    display:block;
    position:relative;
    right:0;
    transform:none;
    color:white;
    transition:0.3s;
}
.message2ndTitle > .fa-times-circle:hover{
    color:red
}
.messageListBox {
    position:relative;
    width: 100%;
    background-color: #a7784c52;
    transition:0.3s;
    border-bottom: 1px solid rgb(248, 248, 248);
    display:flex;
    padding:2%;
    font-size: 15px;
    color:black;
}
.messageListBox > img {
    margin-right: 2%;
    width: 65px;
    height:65px;
    border-radius: 20%;
    border: 1px solid rgb(156, 156, 156);
}

.messageListBox:hover{
    box-shadow: 5px 5px 5px;
}

.messageListBox > div {
    text-align: left;
}

.messageListList{    
    height:60%;
    font-size: 12px;
    
}

.messageListList :nth-child(1) {    
    width: 250px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;    
    margin-top: auto;
    
    
}

.messageListList :nth-child(2){
    font-size: 12px;
    margin-top: auto;
}

.newAlert {
    position: absolute;
    right:5%;
    top:10%;
    font-size: 10px;
    color:white;
    border-radius: 10px;
    background-color: red;
    padding:2px 3px;
}


</style>