const s = (a) => a[4] * 2 + a[5] * 1.5 + a[6] * 2 + a[7] * 0.5 + a[8] + a[9]; // score function
console.log("Finalists:\n" + require("fs").readFileSync("./input.txt").toString() // read input.txt
    .split('\n').map(l => l.split(',').map(Number)) // load file as matrix of numbers
    .filter(a => a[3] >= 100 && a[9] >= 75)         // remove unqualified apps
    .sort((a, b) => s(b) - s(a)).slice(0, 20)      // sort based on score keeping top 20
    .map((a, i) => `${i + 1}:\t${a}`).join('\n'));   // format for readability

/*
* convert input.txt into an array of arrays of numbers
* remove people who dont get a 100 in cs100 and those with lower than 75 GPA
* sort based on our score function, only 20 finalists survive
* convert array into a pretty string
*/

/* minified: (short enough to be a tweet)
let s=(a)=>a[4]*2+a[5]*1.5+a[6]*2+a[7]*.5+a[8]+a[9]
console.log("Finalists:\n"+require("fs").readFileSync("./input.txt").toString()
.split('\n').map(l=>l.split(',').map(Number)).filter(a=>a[3]>99&&a[9]>74)
.sort((a,b)=>s(b)-s(a)).slice(0,20).map((a,i)=>i+1+": "+a).join('\n'))
*/
