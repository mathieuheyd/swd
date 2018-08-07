/// <reference path="../reference.ts" />

class ActionInterface extends PIXI.Container {

  mulliganAction(onMulligan: Function) {
    let text = new PIXI.Text('Mulligan');
    text.interactive = true;
    text.on('click', onMulligan);
    this.addChild(text);
  }

}
