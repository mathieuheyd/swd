/// <reference path="../reference.ts" />

class ActionInterface extends PIXI.Container {

  mulliganAction(onMulligan: Function) {
    let text = new PIXI.Text('Mulligan');
    text.interactive = true;
    text.on('click', onMulligan);
    this.addChild(text);
  }

  chooseBattlefieldAction() {
    let text = new PIXI.Text('Pick Battlefield');
    this.addChild(text);
  }

  addShieldsAction() {
    let text = new PIXI.Text('Add Shields');
    this.addChild(text);
  }

  noAction() {
    this.removeChildren();
  }

}
