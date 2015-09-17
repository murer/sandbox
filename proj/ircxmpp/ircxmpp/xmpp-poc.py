import sys,xmpp

def messageHandler(conex,msg):
    print "Remetente: " + str(msg.getFrom())
    print "Mensagem: " + str(msg.getBody())
    print msg
    #conex.send(xmpp.Message(msg.getFrom(), 'abc', typ='chat'))

def main():
    gmail_user = 'pyrata@fuweweu.com'
    gmail_pass = '5t6y7u8i'
    
    gtalk_server = "talk.google.com"

    jid=xmpp.protocol.JID(gmail_user)
    print 'domain %s', (jid.getDomain())
    cl=xmpp.Client(jid.getDomain()) # ,debug=[])
    #cl=xmpp.Client('fuweweu.com') # ,debug=[])

    if not cl.connect((gtalk_server,5222)):
        raise IOError(1, "Naoo foi possivel conectar ao servidor.")
    if not cl.auth(jid.getNode(),gmail_pass):
        raise IOError(2, "Houve um erro durante o login. O nome de usuario e a senha estao corretos?")

    cl.sendInitPresence()
    
    cl.RegisterHandler("message", messageHandler)    
    
    cl.send( xmpp.Message( "2fmoa88u8wdvy3kov4nr786ry5@public.talk.google.com" ,"teste", typ='chat') )
    
    while 1:
        try:
            cl.Process(1)
        except KeyboardInterrupt:
            cl.send( xmpp.Message( "2fmoa88u8wdvy3kov4nr786ry5@public.talk.google.com" ,"teste", typ='chat') )
            break
    
    cl.disconnect()

main()