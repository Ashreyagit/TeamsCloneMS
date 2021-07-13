import firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";
// const firebaseConfig = {
//     apiKey: "AIzaSyB13PntUEDMkFbTt2psiHOZtQih83qrf88",
//     authDomain: "whatsapp-clone-fafc9.firebaseapp.com",
//     databaseURL: "https://whatsapp-clone-fafc9.firebaseio.com",
//     projectId: "whatsapp-clone-fafc9",
//     storageBucket: "whatsapp-clone-fafc9.appspot.com",
//     messagingSenderId: "1027981883218",
//     appId: "1:1027981883218:web:f62f32ffe8e5701f0f2c8b",
//     measurementId: "G-0YMF5ZZL8N"
//   };
const firebaseConfig = {
  apiKey: "AIzaSyDJ4n8cG7F2RMeie3u_BCb7ncJqyVx7PHQ",
  authDomain: "onec-9f393.firebaseapp.com",
  databaseURL:"https://onec-9f393-default-rtdb.firebaseio.com/",
  projectId: "onec-9f393",
  storageBucket: "onec-9f393.appspot.com",
  messagingSenderId: "602725617851",
  appId: "1:602725617851:web:bb1864e4ae7369ab68199e",
  measurementId: "G-74J18YXFNH",
};
const firebaseApp = firebase.initializeApp(firebaseConfig);
const db = firebaseApp.firestore();
const auth = firebase.auth();
const provider = new firebase.auth.GoogleAuthProvider();

export { auth, provider };
export default db;
