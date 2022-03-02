GitHub: fork Kotlin-Polytech/KotlinAsFirst2020

Idea: Get from VCS

Idea terminal:
* git branch backport
* git checkout backport
* git remote add upstream-my https://www.github.com/wishyoudie/KotlinAsFirst2021
* git fetch upstream-my
* git rebase --onto backport d535f3592006b8f2593c9f881d72c05164aadc13 upstream-my/master
* git remote add upstream-theirs https://www.github.com/s1ckoleg/KotlinAsFirst
* git fetch upstream-theirs
* git checkout master
* git merge backport
* git merge -s ours upstream-theirs/master
* git remote -v > remotes
* git add remotes
* git commit -m "Add remotes"

Idea: KotlinAsFirst2020 -> New -> File -> "howto.md"

howto.md: all this long manuscript

--- Later ---

Idea terminal:
* git add howto.md
* git commit -m "Add howto.md"
* git push