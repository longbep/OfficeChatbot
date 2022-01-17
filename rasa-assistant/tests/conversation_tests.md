#### This file contains tests to evaluate that your bot behaves as expected.
#### If you want to learn more, please see the docs: https://rasa.com/docs/rasa/user-guide/testing-your-assistant/

## happy path 1
* chao_hoi: hello there!
  - utter_chao_hoi
* mood_great: amazing
  - utter_happy

## happy path 2
* chao_hoi: hello there!
  - utter_chao_hoi
* mood_great: amazing
  - utter_happy
* goodtam_biet: tam_biet-tam_biet!
  - utter_goodtam_biet

## sad path 1
* chao_hoi: hello
  - utter_chao_hoi
* mood_unhappy: not good
  - utter_cheer_up
  - utter_did_that_help
* affirm: yes
  - utter_happy

## sad path 2
* chao_hoi: hello
  - utter_chao_hoi
* mood_unhappy: not good
  - utter_cheer_up
  - utter_did_that_help
* deny: not really
  - utter_goodtam_biet

## sad path 3
* chao_hoi: hi
  - utter_chao_hoi
* mood_unhappy: very terrible
  - utter_cheer_up
  - utter_did_that_help
* deny: no
  - utter_goodtam_biet

## say goodtam_biet
* goodtam_biet: tam_biet-tam_biet!
  - utter_goodtam_biet

## bot challenge
* bot_challenge: are you a bot?
  - utter_iamabot
