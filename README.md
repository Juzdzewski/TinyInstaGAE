# TinyInstaGAE
(par Juzdzewski Matthieu, M1 ALMA)

Rendu du projet TinyInsta -  Google AppEngine


# Contention problem

Par manque de temps, nous n'avons pas pu régler correctement
le problème de contention sur les "Likes". 

Nous avons pensé au sharding mais nous voulions essayer avec
une combinaison de memCache et Queues. Or, en l'état, notre approche
ne fonctionne pas et le temps manque pour explorer l'autre possibilité.

# Fan-out problem

Nous pensons avoir contourner le problème de la manière suivante : 
Si userA et userB follow userC, et userC poste une image, alors ce dernier n'a pas 
besoin d'envoyer quoi que ce soit à ses followers. 
Au lieu de cela, lorsque userA clique sur le bouton "UserC" dans sa liste
"Following" (dans My profile), il va requéter les derniers posts de UserC.
Ainsi, chaque follower va effectuer un simple read sur le datastore. 

# Kinds


