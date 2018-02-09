package entities

import org.scalatest.FlatSpec

class FullDeckSpec extends FlatSpec {

  "A full deck with no characters" should "not be valid" in {
    assert(FullDeck(Array.empty, Array.empty, null).isValid == false)
  }

  "A full deck with no deck" should "not be valid" in {
    assert(FullDeck(Array.empty, Array.empty, null).isValid == false)
  }

  "A full deck with no battlefield" should "not be valid" in {
    assert(FullDeck(Array.empty, Array.empty, null).isValid == false)
  }

}
