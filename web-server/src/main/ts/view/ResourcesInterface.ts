/// <reference path="../reference.ts" />

class ResourcesInterface extends PIXI.Container {

  resources: number = 0;

  constructor() {
    super();
    this.updateDisplay();
  }

  addResources(amount: number) {
    this.resources += amount;
    this.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();
    let text = new PIXI.Text('R: ' + this.resources);
    this.addChild(text);
  }

}