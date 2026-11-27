// Es como un import de librerías, en este caso express
const express = require('express')
const app = express()

// Definimos el puerto en el que se ejecutará la aplicación, que en este caso es el 3000, que es como el puerto por defecto para aplicaciones web
const PORT = 3000


// 
const games = [
    { id: 1, name: 'Among Us', genre: 'Strategy'},
    { id: 2, name: 'Clash Royale', genre: 'Strategy'},
    { id: 1, name: 'Minecraft', genre: 'Adventure'}
]


// Es un endpoint GET
app.get('/games', (req, res) => {
    res.json(games)
})

// Es un mensaje que da la app cuando se ejecuta
app.listen(PORT, () => {
    console.log('App corriendo en el puerto ' + PORT)
})