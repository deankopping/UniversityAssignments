#Katinka Wilkinson, Nathan Bau, Dean Kopping
#Project 2

rm(list=ls())

#RANDOMIZATION (factorial experiments pg 116,117)
treats <- c("p.coke", "g.coke", "p.juice", "g.juice", "p.water", "g.water", "p.swater", "g.swater")
rand <- sample(treats) #puts the treatments into a random order
rand
cbind(1:8, rand) #assigns the treatments to an experimental unit

# READING IN DATA
library(readxl)
icedata <- read_excel("C:/Users/Admin/Desktop/STA2007F/Projects/Project2/icedata.xlsx")
View(icedata)
icedata$Material <- factor(icedata$Material) #Ensuring that R recognizes the treatment factors and blocks as categorical variables (factorial experiments pg 127)
icedata$Drink <- factor(icedata$Drink)
icedata$Block <- factor(icedata$Block)
icedata$Treat <- factor(icedata$Treat)
icedata$Order <- as.integer(icedata$Order)

#CHECKING DATA ASSUMPTIONS (factorial experiments pg 90,91)
#Assumption 1, 2 and 3: no outliers, all population variances are equal, errors are normally distributed
par(mfrow = c(1,1))
boxplot(Time ~ Material, xlab = "material", ylab = "time (seconds)", cex.axis = 1, cex.lab = 1, las = 1, data = icedata) #glass vs plastic
boxplot(Time ~ Drink, xlab = "drink", ylab = "time (seconds)", cex.axis = 1, cex.lab = 1, las = 1, data = icedata) #drinks
boxplot(Time ~ Treat, xlab = "treatment", ylab = "time (seconds)", cex.axis = 0.7, cex.lab = 1, las = 2, data = icedata) #all treatments
#Assumption 4: errors are independent (this is done in the model checking section at the end... is that enough?)
#Assumption 5: the effects of blocks and treatments are additive
plot(icedata$Treat, icedata$Time, las=1, ylab="time(seconds)")
i <- 1 
for(block in unique(icedata$Block)){
  temp <- icedata[icedata$Block == block,]
  temp <- temp[sort(as.numeric(temp$Treat), index.return=TRUE)$ix,]
  lines(temp$Treat, temp$Time, col=i)
  i <- i+1
}

#MODEL CREATION
m1 <- lm(Time ~ Drink*Material + C(Block, contr.sum), data = icedata)
summary(m1) #Where is block 4??? - please look into this

#ANOVA
ANOVA <- aov(m1)

#TUKEY'S PROCEDURE
mat.tuk <- TukeyHSD(ANOVA, which = "Material", ordered=T)
mat.tuk
drink.tuk <- TukeyHSD(ANOVA, which="Drink", ordered=T)
drink.tuk

#CONTRASTS WITH EMMEANS (Factorial experiments pg 129)
library(emmeans)
m2 <- lm(Time ~ Drink*Material + Block, data = icedata)
emmeans(m2, ~ Drink*Material)
emmeans(m2, ~ Drink|Material)
em <- emmeans(m2, ~ Material|Drink) #To be used in the contrast below
em
em.contr <- contrast(em, list(glassvsplastic=c(1,-1,1,-1,1,-1,1,-1)/4), by = NULL) #glass vs plastic contrast
em.contr
#Should I make any other contrasts???

#MODEL CHECKING (factorial experiments pg 108,109)
par(mfrow = c(2,2))
plot(m1) #residual plots
library(lattice)
xyplot(m1$residuals ~ icedata$Order|icedata$Block, ylab = "residuals", xlab = "Order of treament within block", pch=19) #plot of order against residual within blocks


