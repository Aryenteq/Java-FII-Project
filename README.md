# Proiect PA 2024

Studenți:

 1. Gînțu Florin-Ciprian 2A1
 2. Pârlea Marian-Alexandru 2A1


## Descriere proiect:

 **DeliveryPlanner**: Planning the delivery of products purchased online (Vehicle Routing, TSP).
 Proiectul nostru este structura în două părți principale: harta reală a lumii (folosind **OpenStreetMap API**) și varianta clasică (noduri așezate pe ecran în funcție de coordonatele dintr-un fișier .tsp).

Folosind un algoritm genetic, calculăm drumul cel mai optim dintre toate locațiile, ținând cont de încărcătura maximă a vehiculului - în cod există o variabilă care determină numărul de noduri pe care-l poate vitiza o mașină înainte de a se întoarce la depozit.
Când „curierul” termină de livrat coletele din vehicul, acesta se reîntoarce la depozit și continuă să livreze coletele. Vizual, se vor forma mai multe circuite, toate începând (și terminându-se) în nodul de start - locația unde se află depozitul.

## Algoritm genetic
Folosind un algoritm genetic (AG), calculăm ruta cea mai bună. Fiecare candidat primește un cromozom aleatoriu (acesta fiind în realitate doar o permutare).
Fiecărui candidat îi este asociat un fitness, iar selecția este determinată de acest fitness. Cu cât valoarea fitness-ului este mai mare (deci drumul cât mai scurt), cu atât candidatul are șanse mai mari să fie selectat (procente).
La fiecare generație se vor face operațiile clasice unui AG: 

 - Mutație (două alele vor fi interschimbate aleatoriu) - probabiliate 0.01
 - Crossover (PMX) - un algoritm ce asigură că alelele a doi părinți sunt schimbate între ele, menținând totodată și condiția de permutare.
 
 În plus, avem implementate și următoarele optimizări:
 
 - Mutație adaptivă (la un număr de generații, probabilitatea mutației crește)
 - Hipermutație (același concept ca mai sus, dar probabilitatea crește drastic, la 0.1)
 - Elitism (primii 5% (default) din populație trec la următoarea generație garantat, sărind peste selecție)
 - Reverse Elitism (ultimii 5% (default) din populație sunt resetați cu cromozomi noi aleatorii)
 - Wisdom of Crowds (în funcție de orașele vizitate și de ceilalți cromozomi, se va determina o nouă permutare - concept asemănător cu algoritmul Ant Colony Optimization - AOC)
 - 2Opt - dacă algoritmul stagnează prea mult (default 100 generații), luăm patru noduri aleatorii și vedem dacă interschimbarea lor va crea o rută mai bună. În caz afirmativ, facem schimbarea.

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/1b37fef0-b611-4738-90fe-fcc55127c4f0)

## GUI

## I. Varianta clasică - din fișiere .TSP
Când un fișier este selectat, coordonatele vor fi normalizate pentru a încăpea în fereastră. Se creează un graf neorientat cu ponderi pe fiecare muchie. În funcție de parametrii selectați de utilizator, se va folosi un graf custom realizat de noi SAU se va folosi librăria **Graph4J**.
Toți parametrii pot fi schimbați dintr-un meniu special, fiecare proprietate fiind explicată suplimentar dacă userul dă hover.

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/fcc7f951-8063-4d5b-8cb3-07f0cc51fb95)

La apăsarea butonului „Run”, interfața grafică este actualizată - afișând cel mai bun drum găsit. Interfața nu este actualizată la fiecare generație, ci doar la găsirea unui path mai scurt, pentru economisirea resurselor și creșterea performanței (vitezei algoritmului).
Analog, statisticile din stânga sunt actualizate la nevoie (numărul de generații și average path-ul la fiecare generație, best path-ul doar la găsirea unui drum mai scurt).

Un exemplu de algoritm terminat (cu 10 noduri/circuit) pe instanța clasică, berlin52:

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/392c414a-5e11-4059-8da4-559bb67f3397)

Un exemplu de algoritm pe o instanță mai mare, de 783 de noduri:
- Început:

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/265ae109-cfba-48a2-9a0d-0a776d45c6c1)

- Final:

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/a13f9eb9-dce0-4fa4-8eb3-7c8412dee6d9)

## I. Varianta „improved” - folosind harta lumii

 1. Adăugarea de orașe noi în baza de date

Folosim o bază de date care stochează toate orașele selectate (și statusul lor - rezolvate/nerezolvate) + latitudine & longitudine.

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/bdaaba4a-fa48-4da4-83c7-8b88f87f2428)

Există două metode de selecție: 

- Selectarea oriunde pe hartă folosind LMB (click):

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/716b4ecb-d9f6-4485-95ab-eba91fdd9faa)

- Scrierea efectivă a adresei (aceasta va fi căutată și cel mai bun rezultat va fi focusat pe hartă):

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/9a723c03-4b6e-4a00-877a-51858057e7bf)

 2. Selectarea orașelor și rularea algoritmului
Fiecare oraș adăugat în baza de date va fi afișat în această interfață, folosind adresa. La apăsarea LMB, aceste orașe vor fi selectate și afișate în stânga, fiind eliminate de pe hartă.
Dacă utilizatorul se răzgândește, ele pot fi eliminate folosind butonul de „X”.

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/0ba0bae3-dfb9-42ca-ae66-c4ef4ac6f053)

La apăsarea butonului ”Done”, vor fi încărcate toate rutele posibile pentru a elimina din „stresul” algoritmului genetic de a apela continuu API-ul pentru a obține ruta efectivă pe străzile selectate. Aceste rute vor fi încărcate **concurent (folosind Thread-uri)**:

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/831cc2db-7435-4d6f-979c-e5c84018975b)

După finalizarea loading-ului, ruta afișată va fi una aleatorie. User-ul va apăsa click pe „Run” pentru a pune algoritmul genetic în funcțiune. La fel ca la varianta clasică, ruta afișată va fi schimbată când este găsit un circuit mai scurt.

![image](https://github.com/Aryenteq/Java-FII-Project/assets/63722227/992bc7ca-dd6e-47fc-abf4-97a2c6ed95da)

Se poate apăsa butonul de „Mark as solved” pentru a face un update în baza de date, ulterior locațiile nefiind afișate din nou la următoarea rulare a programului.

## Sincronizare GUI - AG

Algoritmul genetic folosește o clasă specială pentru TOȚI parametrii folosiți. Acești parametri vor fi utilizați de interfața grafică, care rulează într-un thread separat. Astfel, fiecare parametru folosește keyword-ul **synchronized**. Această abordare ne oferă posibilitatea de a comunica eficient și în siguranță între cele două componente ale proiectului.


